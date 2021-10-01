package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;

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
  private final CommonChannels commonChannels;

  @Bean
  IntegrationFlow recommendationGeneratedInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .filter(this::filter)
        .transform(this::transform)
        .channel(commonChannels.recommendationGenerated())
        .get();
  }

  private boolean filter(Message<Object> message) {
    // found this condition in the previous code, not sure if payload can be null, though.
    if (message.getPayload() == null) {
      log.trace("Received null recommendation generated message");
      return false;
    }
    return true;
  }

  private RecommendationsGenerated transform(Object payload) {
    try {
      var generated = ((Any) payload).unpack(RecommendationsGenerated.class);
      log.trace("Received generated recommendation message = {}", generated);
      return generated;
    } catch (InvalidProtocolBufferException exception) {
      log.warn("An exception occurred while unpacking {} payload",
          RecommendationsGenerated.class.getName(), exception);
      return null;
    }
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
