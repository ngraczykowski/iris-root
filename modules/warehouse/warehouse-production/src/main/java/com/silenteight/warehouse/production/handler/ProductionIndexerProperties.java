package com.silenteight.warehouse.production.handler;

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
public class ProductionIndexerProperties {

  @NotNull
  private Integer productionBatchSize;

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties productionIndexedOutbound;

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties productionIndexingInbound;
}
