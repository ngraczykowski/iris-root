package com.silenteight.payments.bridge.app.integration.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.payments.bridge.event.RecommendationGeneratedEvent;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(AeInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
@Slf4j
class AeInboundAmqpIntegrationConfiguration {

  @Valid
  private final AeInboundAmqpIntegrationProperties properties;

  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow recommendationGeneratedInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .transform(RecommendationsGenerated.class, RecommendationGeneratedEvent::new)
        .channel(RecommendationGeneratedEvent.CHANNEL)
        .get();
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
