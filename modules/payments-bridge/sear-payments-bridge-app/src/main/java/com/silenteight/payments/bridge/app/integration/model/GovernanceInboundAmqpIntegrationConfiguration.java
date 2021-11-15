package com.silenteight.payments.bridge.app.integration.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.payments.bridge.event.ModelUpdatedEvent;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import static com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel.fromSolvingModel;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(GovernanceInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
@Slf4j
class GovernanceInboundAmqpIntegrationConfiguration {

  @Valid
  private final GovernanceInboundAmqpIntegrationProperties properties;
  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow modelPromotedForProductionInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .transform(SolvingModel.class, this::buildModelEvent)
        .channel(ModelUpdatedEvent.CHANNEL)
        .get();
  }

  @Nonnull
  private ModelUpdatedEvent buildModelEvent(SolvingModel model) {
    log.info("Received updated production solving model: policy={}, categories={}, features={}",
        model.getPolicyName(), model.getCategoriesList(),
        model.getFeaturesList().stream().map(Feature::getName));

    var event = new ModelUpdatedEvent();
    event.registerCollector(SolvingModel.class, () -> fromSolvingModel(model));
    return event;
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
