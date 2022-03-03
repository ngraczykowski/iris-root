package com.silenteight.customerbridge.common.decisiongroups;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.MessageSenderFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.EXCHANGE_ALERT_METADATA;

@Configuration
@RequiredArgsConstructor
class DecisionGroupsConfiguration {

  @NonNull
  private final MessageSenderFactory messageSenderFactory;

  @Bean
  DecisionGroupsService decisionGroupsService(DecisionGroupsReader reader) {
    return new DecisionGroupsService(reader, messageSenderFactory.get(EXCHANGE_ALERT_METADATA));
  }
}
