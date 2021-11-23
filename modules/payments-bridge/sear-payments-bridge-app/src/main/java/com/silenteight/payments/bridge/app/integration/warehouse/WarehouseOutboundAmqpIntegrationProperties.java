package com.silenteight.payments.bridge.app.integration.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.silenteight.payments.bridge.warehouse.index.model.WarehouseIndexRequestedEvent.IndexRequestOrigin;

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

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Origins requestOrigin = new Origins();

  @Data
  static class Command {

    @NotBlank
    private String outboundExchangeName = BRIDGE_WAREHOUSE_EXCHANGE_NAME;

    @NotNull
    private String alertStoredRoutingKey = BRIDGE_WAREHOUSE_ROUTING_KEY;
  }

  @Data
  static class Origins {
    private Priority learning = new Priority(3);
    private Priority cmapi = new Priority(7);
    private Priority unset = new Priority(1);
  }

  @Data
  @AllArgsConstructor
  static class Priority {
    private int priority;
  }

  public int getPriority(IndexRequestOrigin origin) {
    switch (origin) {
      case UNSET:
        return requestOrigin.getUnset().getPriority();
      case CMAPI:
        return requestOrigin.getCmapi().getPriority();
      case LEARNING:
        return requestOrigin.getLearning().getPriority();
      default:
        throw new IllegalArgumentException("Unknown indexRequestOrigin type: " + origin);
    }
  }

}
