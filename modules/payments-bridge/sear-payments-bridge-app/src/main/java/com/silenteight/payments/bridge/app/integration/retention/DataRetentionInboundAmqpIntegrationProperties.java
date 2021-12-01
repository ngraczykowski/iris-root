package com.silenteight.payments.bridge.app.integration.retention;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.DATA_RETENTION_QUEUE_NAME;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.data-retention.inbound")
class DataRetentionInboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private DataRetentionInboundAmqpIntegrationProperties.Commands
      commands = new DataRetentionInboundAmqpIntegrationProperties.Commands();

  String[] getInboundQueueNames() {
    return new String[] {
        commands.getInboundQueueName(),
        };
  }

  @Data
  static class Commands {

    @NotBlank
    private String inboundQueueName = DATA_RETENTION_QUEUE_NAME;
  }
}
