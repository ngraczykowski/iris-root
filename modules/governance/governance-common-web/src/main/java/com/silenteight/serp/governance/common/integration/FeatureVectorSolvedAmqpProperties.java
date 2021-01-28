package com.silenteight.serp.governance.common.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.feature-vector.integration")
class FeatureVectorSolvedAmqpProperties {

  @NestedConfigurationProperty
  @Valid
  @NotNull
  AmqpOutboundProperties request = new AmqpOutboundProperties();

  @NestedConfigurationProperty
  @Valid
  @NotNull
  AmqpInboundProperties receive = new AmqpInboundProperties();
}
