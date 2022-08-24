package ru.neirodev.mehanik;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MehanikApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		String del = "|";
		Character c = '|';
		System.out.println(del.charAt(0));
		//SpringApplication.run(MehanikApplication.class, args);
	}

}
