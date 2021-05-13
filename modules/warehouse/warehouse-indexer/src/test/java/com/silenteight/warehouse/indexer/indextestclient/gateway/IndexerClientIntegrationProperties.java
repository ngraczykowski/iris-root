package com.silenteight.warehouse.indexer.indextestclient.gateway;

import lombok.Data;

import com.silenteight.warehouse.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "test.messaging.integration")
public class IndexerClientIntegrationProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties productionIndexingTestClientOutbound;
  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties simulationIndexingTestClientOutbound;
}
