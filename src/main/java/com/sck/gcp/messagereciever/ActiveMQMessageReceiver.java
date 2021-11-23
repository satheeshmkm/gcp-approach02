package com.sck.gcp.messagereciever;

import java.io.IOException;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.activemq.Message;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.sck.gcp.processor.FileProcessor;
import com.sck.gcp.service.BigDataService;
import com.sck.gcp.service.CloudStorageService;

@Component
public class ActiveMQMessageReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQMessageReceiver.class);

	@Autowired
	private FileProcessor fileProcessor;

	@Autowired
	private BigDataService bigDataService;

	@Value("${com.sck.upload.dir:upload}")
	private String uploadFolder;

	@Value("${com.sck.upload.backup.dir:backup}")
	private String backupFolder;

	@Value("${sck.gcp.bigquery.transfer.name}")
	private String bqTransferName;

	@Autowired
	private CloudStorageService cloudStorageService;

	@JmsListener(destination = "${inbound.endpoint}", containerFactory = "jmsListenerContainerFactory")
	public void receiveMessage(Message msg) throws JMSException {
		try {
			String xml = ((TextMessage) msg).getText();
			LOGGER.info("readFile() completed");

			JSONArray jsonProducts = fileProcessor.convertToJSONs(xml);
			String jsonl = fileProcessor.convertToJSONL(jsonProducts);
			LOGGER.info("convertToJSONL() completed");

			byte[] arr = jsonl.getBytes();
			String uploadFilePath = cloudStorageService.getFilePath(backupFolder, "product");
			cloudStorageService.uploadToCloudStorage(uploadFilePath, arr);
			LOGGER.info("File uploaded to bucket with name " + uploadFilePath);

			String backupFilePath = cloudStorageService.getFilePath(backupFolder, "product");
			cloudStorageService.copyToOtherBucket(uploadFilePath, backupFilePath);
			LOGGER.info("File copied from " + uploadFilePath + " to " + backupFilePath);

			List<String> runs = bigDataService.runTransfer(bqTransferName);
			LOGGER.info("Job started " + runs);

		} catch (JMSException e) {
			LOGGER.error("JMSException occured", e);
		} catch (IOException e) {
			LOGGER.error("JMSException occured", e);
		}

	}

}
