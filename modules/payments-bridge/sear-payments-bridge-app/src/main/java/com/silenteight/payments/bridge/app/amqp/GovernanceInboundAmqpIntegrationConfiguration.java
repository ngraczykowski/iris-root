package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.payments.bridge.governance.core.solvingmodel.port.ReceiveCurrentModelUseCase;
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
@EnableConfigurationProperties(GovernanceInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
@Slf4j
class GovernanceInboundAmqpIntegrationConfiguration {

  @Valid
  private final GovernanceInboundAmqpIntegrationProperties properties;

  private final AmqpInboundFactory inboundFactory;

  private final ReceiveCurrentModelUseCase receiveCurrentModelUseCase;

  @Bean
  IntegrationFlow modelPromotedForProductionInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .handle(message -> {
          var payload = message.getPayload();
          try {
            var solvingModel =
                ((Any) payload).unpack(SolvingModel.class);
            log.trace(
                "Received model promoted for production message = {}",
                solvingModel);
            receiveCurrentModelUseCase.handleModelPromotedForProductionMessage(
                solvingModel);
          } catch (InvalidProtocolBufferException e) {
            log.error(
                "Unable to unpack model promoted for production message");
          }
        })
        .get();
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
