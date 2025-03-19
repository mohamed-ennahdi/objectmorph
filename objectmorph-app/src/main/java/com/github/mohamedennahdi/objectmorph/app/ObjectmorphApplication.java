package com.github.mohamedennahdi.objectmorph.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ObjectmorphApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObjectmorphApplication.class, args);
	}
}
