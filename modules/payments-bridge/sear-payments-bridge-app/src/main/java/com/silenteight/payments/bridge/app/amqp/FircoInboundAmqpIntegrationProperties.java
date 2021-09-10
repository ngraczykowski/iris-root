package com.silenteight.payments.bridge.app.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_COMMAND_QUEUE_NAME;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.firco.integration.inbound")
class FircoInboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Commands commands = new Commands();

  String[] getInboundQueueNames() {
    return new String[] {
        commands.getInboundQueueName(),
        };
  }

  @Data
  static class Commands {

    @NotBlank
    private String inboundQueueName = FIRCO_COMMAND_QUEUE_NAME;
  }
}
