package com.silenteight.warehouse.test.client.gateway;

import lombok.Data;

import com.silenteight.warehouse.test.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties("test.messaging.integration")
public class IndexerClientIntegrationProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties productionIndexingTestClientOutbound;
  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties simulationIndexingTestClientOutbound;
  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties personalInformationExpiredIndexingTestClientOutbound;
  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties analysisExpiredIndexingTestClientOutbound;
  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties alertsExpiredIndexingTestClientOutbound;
  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties qaIndexingTestClientOutbound;
  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties qaIndexingInternalTestClientOutbound;
}
