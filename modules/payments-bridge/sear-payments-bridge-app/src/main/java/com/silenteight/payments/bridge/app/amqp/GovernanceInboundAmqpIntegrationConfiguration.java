package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.ModelUpdatedEvent;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(GovernanceInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
@Slf4j
class GovernanceInboundAmqpIntegrationConfiguration {

  @Valid
  private final GovernanceInboundAmqpIntegrationProperties properties;

  private final AmqpInboundFactory inboundFactory;
  private final CommonChannels commonChannels;

  @Bean
  IntegrationFlow modelPromotedForProductionInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .transform(SolvingModel.class, this::buildModelEvent)
        .channel(commonChannels.solvingModelUpdated())
        .get();
  }

  @Nonnull
  private ModelUpdatedEvent buildModelEvent(SolvingModel model) {
    var event = new ModelUpdatedEvent();
    event.registerCollector(SolvingModel.class, () -> model);
    return event;
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
