package com.silenteight.warehouse.test.client.listener;

import lombok.Data;

import com.silenteight.warehouse.test.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "test.messaging.integration")
class IndexedEventIntegrationProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties productionIndexedEventTestListenerInbound;

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties simulationIndexedEventTestListenerInbound;
}
