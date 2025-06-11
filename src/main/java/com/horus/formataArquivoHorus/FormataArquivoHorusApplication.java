package com.horus.formataArquivoHorus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.horus.formataArquivoHorus")
@EnableJpaRepositories(basePackages = "com.horus.formataArquivoHorus")
public class FormataArquivoHorusApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormataArquivoHorusApplication.class, args);
	}

}
