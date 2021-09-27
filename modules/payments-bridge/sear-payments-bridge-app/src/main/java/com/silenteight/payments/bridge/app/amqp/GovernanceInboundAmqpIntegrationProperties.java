package com.silenteight.payments.bridge.app.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.BRIDGE_MODEL_PROMOTED_PRODUCTION_QUEUE_NAME;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.governance.integration.inbound")
class GovernanceInboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private GovernanceInboundAmqpIntegrationProperties.Commands
      commands = new GovernanceInboundAmqpIntegrationProperties.Commands();

  String[] getInboundQueueNames() {
    return new String[] {
        commands.getInboundQueueName(),
        };
  }

  @Data
  static class Commands {

    @NotBlank
    private String inboundQueueName = BRIDGE_MODEL_PROMOTED_PRODUCTION_QUEUE_NAME;
  }
}
