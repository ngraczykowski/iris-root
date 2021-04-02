package com.silenteight.warehouse.indexer;

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
@ConfigurationProperties(prefix = "warehouse.messaging.integration")
public class IndexerIntegrationProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  AmqpOutboundProperties alertIndexedOutbound = new AmqpOutboundProperties();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  AmqpInboundProperties alertIndexingInbound = new AmqpInboundProperties();
}
