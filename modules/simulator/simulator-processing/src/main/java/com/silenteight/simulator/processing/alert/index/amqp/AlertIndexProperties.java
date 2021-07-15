package com.silenteight.simulator.processing.alert.index.amqp;

import lombok.Data;

import com.silenteight.simulator.common.integration.AmqpInboundProperties;
import com.silenteight.simulator.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "simulator.processing.alert")
public class AlertIndexProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties recommendationsInbound;

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties recommendationsOutbound;

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties ackMessagesInbound;
}
