package com.silenteight.warehouse.retention.production.personalinformation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({ PersonalInformationIntegrationProperties.class,
    PersonalInformationProperties.class})
class RetentionPersonalInformationListenerConfiguration {

  private static final String RETENTION_PII_INBOUND_CHANNEL =
      "retentionPersonalInformationInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  @Valid
  private final PersonalInformationIntegrationProperties integrationProperties;

  @Bean
  IntegrationFlow personalInformationExpiredQueueToChannelIntegrationFlow(
      PersonalInformationExpiredDataHandler handler) {

    return createInputFlow(
        RETENTION_PII_INBOUND_CHANNEL,
        integrationProperties.getPersonalInformationExpiredIndexingInbound(),
        handler);
  }

  private IntegrationFlow createInputFlow(String channel,
      AmqpInboundProperties properties,
      PersonalInformationExpiredDataHandler handler) {
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
  PersonalInformationExpiredDataHandler personalInformationExpiredDataHandler(
      ErasePersonalInformationUseCase erasePersonalInformationUseCase) {

    return new PersonalInformationExpiredDataHandler(erasePersonalInformationUseCase);
  }
}
