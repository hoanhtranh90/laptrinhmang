package com.core.config;

import com.core.utils.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.modelmapper.ModelMapper;
/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Locale;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * @author sadfsafbhsaid
 */
@Configuration
public class ApplicationConfig {
    @Value("${readTimeout}")
    private Integer readTimeout;

    @Value("${connectionTimeout}")
    private Integer connectionTimeout;
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setUseOSGiClassLoaderBridging(true)
                .setSkipNullEnabled(true)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE);
        return modelMapper;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);




        mailSender.setUsername(Constants.EMAIL.EMAIL_USER);
        mailSender.setPassword(Constants.EMAIL.EMAIL_PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        return mailSender;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory("http://localhost:8080/mock");
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        return restTemplate;
    }
    @Bean
    public MessageSourceEn messageSourceEn() {

        return new MessageSourceEn();
    }

    /**
     * Bean read message properties
     *
     * @return
     */
    @Bean
    public MessageSourceVi messageSourceVi() {

        return new MessageSourceVi();
    }

    /**
     * Bean to custom message read from message properties
     *
     * @return
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSourceEn().getMessageSource());
        return bean;
    }
    @Bean
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        requestFactory.setReadTimeout(readTimeout * 10000);
        requestFactory.setConnectTimeout(connectionTimeout * 10000);
        return restTemplate;
    }
    @Getter
    @Setter
    public class MessageSourceEn {

        private MessageSource messageSource;

        public MessageSourceEn() {
            ReloadableResourceBundleMessageSource bundleMessageSource
                    = new ReloadableResourceBundleMessageSource();
            bundleMessageSource.setBasename("classpath:messages_en");
            bundleMessageSource.setDefaultEncoding("UTF-8");
            this.messageSource = bundleMessageSource;
        }

        public String getMessageEn(String codeMsg) {
            return messageSource.getMessage(codeMsg, null, Locale.ENGLISH);
        }

        public String getMessageEn(String codeMsg, @Nullable Object[] args) {
            return messageSource.getMessage(codeMsg, args, Locale.ENGLISH);
        }
    }

    @Getter
    @Setter
    public class MessageSourceVi {

        private MessageSource messageSource;

        public MessageSourceVi() {
            ReloadableResourceBundleMessageSource bundleMessageSource
                    = new ReloadableResourceBundleMessageSource();
            bundleMessageSource.setBasename("classpath:messages_vi");
            bundleMessageSource.setDefaultEncoding("UTF-8");
            this.messageSource = bundleMessageSource;
        }

        public String getMessageVi(String codeMsg) {
            return messageSource.getMessage(codeMsg, null, new Locale("vi", "VN"));
        }

        public String getMessageVi(String codeMsg, @Nullable Object[] args) {
            return messageSource.getMessage(codeMsg, args, new Locale("vi", "VN"));
        }
    }

}
