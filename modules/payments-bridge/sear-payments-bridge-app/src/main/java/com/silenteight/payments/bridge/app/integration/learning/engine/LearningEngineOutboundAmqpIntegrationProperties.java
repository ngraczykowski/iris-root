package com.silenteight.payments.bridge.app.integration.learning.engine;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.LEARNING_ENGINE_EXCHANGE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.LEARNING_ENGINE_HISTORICAL_DECISION_ROUTING_KEY;

@Data
@Validated
@ConfigurationProperties(prefix = "learning.engine.integration.outbound")
class LearningEngineOutboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private Command command = new Command();

  @Data
  static class Command {

    @NotBlank
    private String outboundExchangeName = LEARNING_ENGINE_EXCHANGE_NAME;

    @NotNull
    private String learningEngineHistoricalDecisionRoutingKey =
        LEARNING_ENGINE_HISTORICAL_DECISION_ROUTING_KEY;
  }

}
