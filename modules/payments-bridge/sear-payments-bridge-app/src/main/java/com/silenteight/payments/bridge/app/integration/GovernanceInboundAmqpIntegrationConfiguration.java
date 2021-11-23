package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

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
  private final ModelUpdateIntegrationService modelUpdateIntegrationService;

  @Bean
  IntegrationFlow modelPromotedForProductionInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .handle(SolvingModel.class, (payload, headers) -> {
          log.info(
              "Received updated production solving model: policy={}, categories={}, features={}",
              payload.getPolicyName(), payload.getCategoriesList(),
              payload.getFeaturesList().stream().map(Feature::getName));
          modelUpdateIntegrationService.analysisModelUpdated(payload);
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
