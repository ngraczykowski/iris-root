package com.silenteight.payments.bridge.app.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.COMMAND_EXCHANGE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.EVENT_EXCHANGE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.EVENT_INTERNAL_EXCHANGE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_ACCEPT_ALERT_ROUTING_KEY;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.firco.integration.outbound")
class FircoOutboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Command command = new Command();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Event event = new Event();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private EventInternal eventInternal = new EventInternal();

  @Data
  static class Command {

    @NotBlank
    private String outboundExchangeName = COMMAND_EXCHANGE_NAME;

    @NotNull
    private String acceptAlertRoutingKey = FIRCO_ACCEPT_ALERT_ROUTING_KEY;
  }

  @Data
  static class Event {

    @NotBlank
    private String outboundExchangeName = EVENT_EXCHANGE_NAME;
  }

  @Data
  static class EventInternal {

    @NotBlank
    private String outboundExchangeName = EVENT_INTERNAL_EXCHANGE_NAME;
  }
}
