package com.silenteight.warehouse.qa.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.warehouse.qa.processing.QaProcessingConfiguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(QaMessageHandlerProperties.class)
@RequiredArgsConstructor
@Import(QaProcessingConfiguration.class)
class QaMessageHandlerConfiguration {

  public static final String QA_INDEXING_INBOUND_CHANNEL = "qaIndexingInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @NonNull
  @Valid
  private final QaMessageHandlerProperties properties;

  @Bean
  IntegrationFlow qaIndexingQueueToChannelIntegrationFlow() {
    return createInputFlow(
        QA_INDEXING_INBOUND_CHANNEL,
        properties.getQaIndexingInbound().getQueueName());
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .get();
  }

  @Bean
  QaRequestCommandIntegrationFlowAdapter qaRequestCommandIntegrationFlowAdapter(
      QaRequestCommandHandler qaRequestCommandHandler) {

    return new QaRequestCommandIntegrationFlowAdapter(
        qaRequestCommandHandler,
        QA_INDEXING_INBOUND_CHANNEL);
  }
}
