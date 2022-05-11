package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JavaMailSenderProperties.class)
@Profile("!mockemail")
class JavaMailSenderConfiguration {

  private final JavaMailSenderProperties javaMailSenderProperties;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(javaMailSenderProperties.getHost());
    mailSender.setPort(javaMailSenderProperties.getPort());
    mailSender.setUsername(javaMailSenderProperties.getUsername());
    mailSender.setPassword(javaMailSenderProperties.getPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }
}
