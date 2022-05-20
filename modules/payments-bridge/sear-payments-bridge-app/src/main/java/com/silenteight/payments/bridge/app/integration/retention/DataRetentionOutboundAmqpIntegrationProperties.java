package com.silenteight.payments.bridge.app.integration.retention;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.DATA_RETENTION_ALERT_EXPIRED_ROUTING_KEY;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.DATA_RETENTION_EXCHANGE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.DATA_RETENTION_PERSONAL_INFORMATION_EXPIRED_ROUTING_KEY;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.data-retention.integration.outbound")
class DataRetentionOutboundAmqpIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private PersonalInformationCommand personalInformationCommand = new PersonalInformationCommand();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  private AlertDataCommand alertDataCommand = new AlertDataCommand();


  @Data
  static class PersonalInformationCommand {

    @NotBlank
    private String outboundExchangeName = DATA_RETENTION_EXCHANGE_NAME;

    @NotNull
    private String routingKey = DATA_RETENTION_PERSONAL_INFORMATION_EXPIRED_ROUTING_KEY;
  }

  @Data
  static class AlertDataCommand {

    @NotBlank
    private String outboundExchangeName = DATA_RETENTION_EXCHANGE_NAME;

    @NotNull
    private String routingKey = DATA_RETENTION_ALERT_EXPIRED_ROUTING_KEY;
  }

}
