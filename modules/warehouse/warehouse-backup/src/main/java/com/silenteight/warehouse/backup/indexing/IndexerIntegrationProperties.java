package com.silenteight.warehouse.backup.indexing;

import lombok.Data;

import com.silenteight.warehouse.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.messaging.integration")
public class IndexerIntegrationProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties backupIndexingInbound;
}
