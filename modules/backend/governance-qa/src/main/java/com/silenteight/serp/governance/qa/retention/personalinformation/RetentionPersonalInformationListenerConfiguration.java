package com.silenteight.serp.governance.qa.retention.personalinformation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.serp.governance.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(PersonalInformationIntegrationProperties.class)
class RetentionPersonalInformationListenerConfiguration {

  private static final String QA_RETENTION_PII_INBOUND_CHANNEL =
      "qaRetentionPersonalInformationInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow qaRetentionPersonalInformationQueueToChannelIntegrationFlow(
      @Valid PersonalInformationIntegrationProperties properties,
      PersonalInformationExpiredCommandHandler personalInformationExpiredCommandHandler) {

    return createInputFlow(QA_RETENTION_PII_INBOUND_CHANNEL, properties.getReceive(),
        personalInformationExpiredCommandHandler);
  }

  private IntegrationFlow createInputFlow(String channel,
      AmqpInboundProperties properties, PersonalInformationExpiredCommandHandler handler) {

    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(properties.getQueueName())))
        .channel(channel)
        .handle(
            PersonalInformationExpired.class,
            (payload, headers) -> {
              handler.handle(payload);
              return null;
            })
        .get();
  }

  @Bean
  PersonalInformationExpiredCommandHandler personalInformationExpiredDataHandler(
      EraseDecisionCommentUseCase eraseDecisionCommentUseCase) {

    return new PersonalInformationExpiredCommandHandler(eraseDecisionCommentUseCase);
  }
}
