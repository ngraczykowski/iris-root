package com.silenteight.serp.governance.qa.retention.alert;

import lombok.Data;

import com.silenteight.serp.governance.common.integration.AmqpInboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.qa.retention.alerts-expired.integration")
@ConstructorBinding
class AlertsExpiredIntegrationProperties {

  @Valid
  @NestedConfigurationProperty
  private AmqpInboundProperties receive;
}
