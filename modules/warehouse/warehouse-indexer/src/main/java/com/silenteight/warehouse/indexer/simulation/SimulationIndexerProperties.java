package com.silenteight.warehouse.indexer.simulation;

import lombok.Data;

import com.silenteight.warehouse.common.integration.AmqpInboundProperties;
import com.silenteight.warehouse.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexer")
public class SimulationIndexerProperties {

  @NotNull
  private Integer simulationBatchSize;

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties simulationIndexedOutbound;

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties simulationIndexingInbound;
}
