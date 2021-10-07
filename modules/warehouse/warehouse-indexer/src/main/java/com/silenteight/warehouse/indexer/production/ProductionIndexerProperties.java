package com.silenteight.warehouse.indexer.production;

import lombok.Data;

import com.silenteight.warehouse.common.integration.AmqpInboundProperties;
import com.silenteight.warehouse.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexer")
public class ProductionIndexerProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties productionIndexedOutbound;

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties productionIndexingInbound;
}
