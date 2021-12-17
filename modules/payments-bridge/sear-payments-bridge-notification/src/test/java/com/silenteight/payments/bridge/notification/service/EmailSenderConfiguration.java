package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(EmailSenderProperties.class)
class EmailSenderConfiguration {

  private final EmailSenderProperties emailSenderProperties;

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(emailSenderProperties.getHost());
    mailSender.setPort(emailSenderProperties.getPort());

    mailSender.setUsername(emailSenderProperties.getUsername());
    mailSender.setPassword(emailSenderProperties.getPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }
}
