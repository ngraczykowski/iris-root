package com.silenteight.payments.bridge.app.integration.response;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_EXCHANGE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_RESPONSE_COMPLETED_ROUTING_KEY;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.firco.integration.outbound.response")
class FircoOutboundAmqpIntegrationProperties {

  @NotBlank
  private String outboundExchangeName = FIRCO_EXCHANGE_NAME;

  @NotNull
  private String responseCompletedRoutingKey = FIRCO_RESPONSE_COMPLETED_ROUTING_KEY;

}
