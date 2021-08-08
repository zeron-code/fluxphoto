package org.fluxphoto;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
public class AppConfiguration {

	private static final String MONGO_DB_URL = "mongodb://localhost";
	private static final String MONGO_DB_NAME = "test";
	
	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				.baseUrl("https://jsonplaceholder.typicode.com")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	@Bean("taskExecutor")
	public Executor threadPoolTaskExecutor() {
		// Tuning thread pool omitted for brevity
		return new ThreadPoolTaskExecutor();
	}

	@Bean
	public MongoClient reactiveMongoClient() {
		return MongoClients.create(MONGO_DB_URL);
	}

	@Bean 
	public ReactiveMongoTemplate reactiveMongoTemplate() {
		return new ReactiveMongoTemplate(reactiveMongoClient(), MONGO_DB_NAME);
	}
	
}
