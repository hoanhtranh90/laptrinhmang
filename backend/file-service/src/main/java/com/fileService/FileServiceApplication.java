package com.fileService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScan({"com.arfm.core.*","com.arfm.fileService.*"})
@ComponentScan(basePackages = {"com.*"})
@EntityScan("com.core.entity")
@EnableJpaRepositories(basePackages = {"com.core.repository", "com.core.repository.impl"})
//@EnableJpaRepositories(basePackages = {"com.arfm.core.repository", "com.arfm.core.repository.impl"})
public class FileServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);
    }
}
