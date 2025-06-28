package com.ege.student_info_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.ege") // Tüm alt paketleri tara
@EntityScan(basePackages = "com.ege.entities") // Entity'leri tara
@EnableJpaRepositories(basePackages = "com.ege.repository") // Repository'leri tara
public class StudentInfoSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentInfoSystemApplication.class, args);
	}
}