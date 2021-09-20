package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.alertmessage.integration.AlertMessageChannels;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.IssueRecommendationUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(FircoInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class FircoInboundAmqpIntegrationConfiguration {

  @Valid
  private final FircoInboundAmqpIntegrationProperties properties;

  private final AmqpInboundFactory inboundFactory;

  private final IssueRecommendationUseCase issueRecommendationUseCase;

  @Bean
  IntegrationFlow alertMessageStoredReceivedInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .channel(AlertMessageChannels.ALERT_MESSAGE_STORED_RECEIVED_INBOUND_CHANNEL)
        .handle(MessageStored.class, (payload, headers) -> {
          issueRecommendationUseCase.issue(payload);
          return null;
        })
        .get();
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
