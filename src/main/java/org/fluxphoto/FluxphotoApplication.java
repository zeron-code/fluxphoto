package org.fluxphoto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableAsync
public class FluxphotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FluxphotoApplication.class, args);
	}
}
