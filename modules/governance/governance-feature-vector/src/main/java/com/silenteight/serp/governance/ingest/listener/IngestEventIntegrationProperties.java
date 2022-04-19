package com.silenteight.serp.governance.ingest.listener;

import lombok.Data;

import com.silenteight.serp.governance.common.integration.AmqpInboundProperties;
import com.silenteight.serp.governance.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.ingest.integration")
@ConstructorBinding
public class IngestEventIntegrationProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties receive;

  @Valid
  @NestedConfigurationProperty
  private AmqpOutboundProperties request;
}
