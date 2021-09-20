package com.silenteight.payments.bridge.svb.learning.notification.service.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.AmazonSesClient;
import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.SendNotificationUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SendNotificationProperties.class)
class SendNotificationConfiguration {

  @Valid
  private final SendNotificationProperties properties;

  private final AmazonSesClient amazonSesClient;
  
  @Bean
  SendNotificationUseCase sendNotificationUseCase() {
    return new SendNotificationService(amazonSesClient, properties);
  }
}
