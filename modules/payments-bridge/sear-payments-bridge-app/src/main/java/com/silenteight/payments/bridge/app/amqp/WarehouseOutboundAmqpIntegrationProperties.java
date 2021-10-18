package com.silenteight.payments.bridge.app.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.BRIDGE_WAREHOUSE_EXCHANGE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.BRIDGE_WAREHOUSE_ROUTING_KEY;

@Data
@Validated
@ConfigurationProperties(prefix = "wh.integration.outbound")
class WarehouseOutboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Command command = new Command();

  @Data
  static class Command {

    @NotBlank
    private String outboundExchangeName = BRIDGE_WAREHOUSE_EXCHANGE_NAME;

    @NotNull
    private String alertStoredRoutingKey = BRIDGE_WAREHOUSE_ROUTING_KEY;
  }
}
