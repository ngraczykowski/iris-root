package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@Profile("mockemail")
class MockedJavaMailSender {

  private static final String HOST = "localhost";
  private static final int PORT = 24170;
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";

  @Bean
  public JavaMailSender mockedSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(HOST);
    mailSender.setPort(PORT);
    mailSender.setUsername(USERNAME);
    mailSender.setPassword(PASSWORD);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }
}
