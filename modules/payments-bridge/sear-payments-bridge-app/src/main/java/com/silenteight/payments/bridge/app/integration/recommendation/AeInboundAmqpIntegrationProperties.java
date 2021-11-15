package com.silenteight.payments.bridge.app.integration.recommendation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.BRIDGE_RECOMMENDATION_QUEUE_NAME;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.ae.integration.inbound")
class AeInboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private AeInboundAmqpIntegrationProperties.Commands
      commands = new AeInboundAmqpIntegrationProperties.Commands();

  String[] getInboundQueueNames() {
    return new String[] {
        commands.getInboundQueueName(),
        };
  }

  @Data
  static class Commands {

    @NotBlank
    private String inboundQueueName = BRIDGE_RECOMMENDATION_QUEUE_NAME;
  }
}
