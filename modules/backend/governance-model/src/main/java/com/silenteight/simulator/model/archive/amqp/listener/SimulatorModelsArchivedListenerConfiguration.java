package com.silenteight.simulator.model.archive.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.simulator.model.archive.amqp.ModelArchivingProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ModelArchivingProperties.class)
class SimulatorModelsArchivedListenerConfiguration {

  private static final String MODELS_ARCHIVED_INBOUND_CHANNEL = "modelsArchivedInboundChannel";

  @NonNull
  @Valid
  private final ModelArchivingProperties properties;

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow simulatorModelsArchivedMessagesQueueToChannelIntegrationFlow() {
    return createInputFlow(
        MODELS_ARCHIVED_INBOUND_CHANNEL,
        properties.modelsArchivedInboundQueueName());
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
  ModelsArchivedFlowAdapter simulatorModelsArchivedMessagesCommandIntegrationFlowAdapter(
      SimulatorModelsArchivedMessageHandler handler) {

    return new ModelsArchivedFlowAdapter(MODELS_ARCHIVED_INBOUND_CHANNEL, handler);
  }
}
