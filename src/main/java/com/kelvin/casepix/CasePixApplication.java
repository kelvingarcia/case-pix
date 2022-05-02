package com.kelvin.casepix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class CasePixApplication {

	public static void main(String[] args) {
		BlockHound.install();
		SpringApplication.run(CasePixApplication.class, args);
	}

}
