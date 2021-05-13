package com.silenteight.warehouse.indexer.indextestclient.listener;

import lombok.Data;

import com.silenteight.warehouse.common.integration.AmqpInboundProperties;

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
  private AmqpInboundProperties alertIndexedEventTestListenerInbound;
}
