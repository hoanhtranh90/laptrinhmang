/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fileService.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 *
 * @author sadfsafbhsaid
 */
@Configuration
public class ApplicationConfigCommon {

     private static final Resource[] DEV_PROPERTIES
            = new ClassPathResource[]{new ClassPathResource("application-dev.properties"),};

    private static final Resource[] TEST_PROPERTIES
            = new FileSystemResource[]{new FileSystemResource("./config/test-env.properties"),};

//  private static final Resource[] PROD_PROPERTIES =
//      new ClassPathResource[] {new ClassPathResource("prod-env.properties"),};
    private static final Resource[] PROD_PROPERTIES
            = new FileSystemResource[]{new FileSystemResource("./config/prod-env.properties"),};

    @Profile("dev")
    public static class DevConfig {

        private DevConfig() {

        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(DEV_PROPERTIES);
            return pspc;
        }
    }

    @Component
    @Profile("test")
    public static class TestConfig {

        private TestConfig() {

        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(TEST_PROPERTIES);

            return pspc;
        }
    }

    @Component
    @Profile("prod")
    public static class ProdConfig {

        private ProdConfig() {
        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(PROD_PROPERTIES);

            return pspc;
        }
    }
}
