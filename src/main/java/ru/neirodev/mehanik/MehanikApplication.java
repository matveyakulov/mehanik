package ru.neirodev.mehanik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MehanikApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MehanikApplication.class, args);
	}

}
