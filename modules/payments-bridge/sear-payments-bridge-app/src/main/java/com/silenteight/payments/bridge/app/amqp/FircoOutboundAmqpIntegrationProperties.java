package com.silenteight.payments.bridge.app.amqp;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_ALERT_STORED_ROUTING_KEY;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_EXCHANGE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_RESPONSE_COMPLETED_ROUTING_KEY;

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
  private Response response = new Response();

  @Data
  static class Command {

    @NotBlank
    private String outboundExchangeName = FIRCO_EXCHANGE_NAME;

    @NotNull
    private String alertStoredRoutingKey = FIRCO_ALERT_STORED_ROUTING_KEY;
  }

  @Data
  static class Response {

    @NotBlank
    private String outboundExchangeName = FIRCO_EXCHANGE_NAME;

    @NotNull
    private String responseCompletedRoutingKey = FIRCO_RESPONSE_COMPLETED_ROUTING_KEY;
  }


}
