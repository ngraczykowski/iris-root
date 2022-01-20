package com.silenteight.warehouse.indexer.production.qa;

import lombok.Data;

import com.silenteight.warehouse.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexer")
public class QaIndexerProperties {

  @NotNull
  private Integer qaBatchSize;

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties qaIndexingInbound;
}