package com.project.letterOfHeart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.project.letterOfHeart.domain.Users;
import com.project.letterOfHeart.repository.repository;

@SpringBootApplication
@EntityScan(basePackageClasses = {Users.class})
@EnableJpaRepositories(basePackageClasses = {repository.class})
public class LetterOfHeartApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetterOfHeartApplication.class, args);
	}

}
