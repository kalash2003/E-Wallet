package com.example.ewallet.userConfig;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class UserKafkaConfig {
	
	Properties getProperties() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return properties;
	}
	
	@Bean
	ObjectMapper geMapper() {
		return new ObjectMapper();
	}
	
	ProducerFactory getProducerFactory() {
		return new DefaultKafkaProducerFactory(getProperties());
	}
	
	@Bean
	KafkaTemplate<String, String> geKafkaTemplate(){
		return new KafkaTemplate(getProducerFactory());
	}

}
