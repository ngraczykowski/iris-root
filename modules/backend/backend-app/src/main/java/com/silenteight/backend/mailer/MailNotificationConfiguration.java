/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.mailer;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(MailNotificationProperties.class)
class MailNotificationConfiguration {

  private final MailNotificationProperties properties;

  @Bean
  AddressedMailMessageCreator addressedMailMessageProvider() {
    return new AddressedMailMessageCreator(properties.getFrom(), properties.getToAddresses());
  }

  @Bean
  SendMailCommandHandler sendMailCommandHandler(AddressedMailMessageCreator messageProvider) {
    return new SendMailCommandHandler(messageProvider);
  }
}
