package com.sck.gcp.service;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

@Service
public class CloudStorageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudStorageService.class);

	@Autowired
	private Storage storage;

	@Value("${com.sck.upload.bucket}")
	private String uploadBucketName;
		

	public void uploadToCloudStorage(String uploadedFile, byte[] arr) {
		BlobId blobId = BlobId.of(uploadBucketName, uploadedFile);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();	
		storage.create(blobInfo, arr);
	}

	public String getFilePath(String uploadFolder, String fileName) {
		Instant instant = Instant.now();
		long timeStampMillis = instant.toEpochMilli();
		
		StringBuilder uploadFilePath = new StringBuilder();
		if (StringUtils.isNotBlank(uploadFolder)) {
			uploadFilePath.append(uploadFolder).append("/");
		}
		uploadFilePath.append(StringUtils.replaceChars(fileName, '.', '_')).append(timeStampMillis).append(".json");
		return  uploadFilePath.toString();
	}
	
}
