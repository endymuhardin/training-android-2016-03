package com.brainmatics.training.bpjs.android;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
    basePackageClasses = {BpjsBackendApplication.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class BpjsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpjsBackendApplication.class, args);
	}
}
