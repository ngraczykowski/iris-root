package com.silenteight.warehouse.retention.simulation.listener;

import lombok.Data;

import com.silenteight.warehouse.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexer")
@ConstructorBinding
class RetentionSimulationIntegrationProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties analysisExpiredIndexingInbound;
}
