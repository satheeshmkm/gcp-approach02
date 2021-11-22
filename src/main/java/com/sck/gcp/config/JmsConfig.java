package com.sck.gcp.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class JmsConfig {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;
	
	@Value("${inbound.endpoint}")
	private String iboundQueue;

	@Bean
	public ConnectionFactory activeMQConnectionFactory() {
		ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerUrl);

		return activeMQConnectionFactory;
	}

	@Bean
	public JmsTransactionManager createTransactionManager(final ConnectionFactory connectionFactory) {
		JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
		jmsTransactionManager.setConnectionFactory(connectionFactory);
		return jmsTransactionManager;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		return new JmsTemplate(activeMQConnectionFactory());
	}

}
