package com.silenteight.payments.bridge.app.integration.store;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_ALERT_STORED_ROUTING_KEY;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_EXCHANGE_NAME;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.firco.integration.outbound.command")
class MessageStoredOutboundAmqpIntegrationProperties {

  @NotBlank
  private String outboundExchangeName = FIRCO_EXCHANGE_NAME;

  @NotNull
  private String alertStoredRoutingKey = FIRCO_ALERT_STORED_ROUTING_KEY;

}
