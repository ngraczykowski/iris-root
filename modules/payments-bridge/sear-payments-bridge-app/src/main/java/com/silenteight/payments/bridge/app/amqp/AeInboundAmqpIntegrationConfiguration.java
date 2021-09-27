package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.payments.bridge.ae.recommendation.port.ReceiveGeneratedRecommendationUseCase;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
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

  private final ReceiveGeneratedRecommendationUseCase receiveGeneratedRecommendationUseCase;

  @Bean
  IntegrationFlow recommendationGeneratedInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .handle(Object.class, (payload, headers) -> {
          if (payload == null) {
            log.trace("Received null recommendation generated message");
            return null;
          }

          try {
            var recommendationGenerated = ((Any) payload).unpack(RecommendationsGenerated.class);
            log.trace("Received generated recommendation message = {}", recommendationGenerated);
            receiveGeneratedRecommendationUseCase.handleGeneratedRecommendationMessage(
                recommendationGenerated);
          } catch (InvalidProtocolBufferException e) {
            return null;
          }

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
