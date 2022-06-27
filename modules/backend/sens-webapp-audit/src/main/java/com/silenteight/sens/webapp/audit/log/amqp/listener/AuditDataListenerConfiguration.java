package com.silenteight.sens.webapp.audit.log.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.log.amqp.AuditLoggingProperties;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuditLoggingProperties.class)
class AuditDataListenerConfiguration {

  private static final String AUDIT_DATA_INBOUND_CHANNEL = "auditDataInboundChannel";

  @NonNull
  @Valid
  private final AuditLoggingProperties properties;

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow auditDataMessagesQueueToChannelIntegrationFlow() {
    return createInputFlow(
        AUDIT_DATA_INBOUND_CHANNEL,
        properties.inboundQueueName());
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
  AuditDataFlowAdapter auditDataMessagesCommandIntegrationFlowAdapter(
      AuditDataMessageHandler handler) {

    return new AuditDataFlowAdapter(AUDIT_DATA_INBOUND_CHANNEL, handler);
  }
}
