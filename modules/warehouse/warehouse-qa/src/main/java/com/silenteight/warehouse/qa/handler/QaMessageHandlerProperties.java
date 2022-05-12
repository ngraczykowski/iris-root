package com.silenteight.warehouse.qa.handler;

import lombok.Data;

import com.silenteight.warehouse.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexer")
public class QaMessageHandlerProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties qaIndexingInbound;
}
