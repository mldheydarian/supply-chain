package com.gts.supplychain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SupplyChainApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyChainApplication.class, args);
	}

}
