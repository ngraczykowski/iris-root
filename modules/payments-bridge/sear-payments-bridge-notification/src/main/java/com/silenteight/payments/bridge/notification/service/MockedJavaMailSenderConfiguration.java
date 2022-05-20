package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

@Configuration
@RequiredArgsConstructor
@Profile("mockemail")
class MockedJavaMailSenderConfiguration {

  @Bean
  public JavaMailSender javaMailSender() {

    return new MockedEmailImplementation();
  }

  class MockedEmailImplementation extends JavaMailSenderImpl {

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) {

    }
  }
}
