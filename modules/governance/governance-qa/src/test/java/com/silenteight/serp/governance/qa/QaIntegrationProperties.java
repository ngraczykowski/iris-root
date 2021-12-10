package com.silenteight.serp.governance.qa;

import lombok.Data;

import com.silenteight.serp.governance.common.integration.AmqpInboundProperties;
import com.silenteight.serp.governance.common.integration.AmqpOutboundProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.qa.integration")
@ConstructorBinding
public class QaIntegrationProperties {

  @Valid
  private AmqpOutboundProperties request;
  @Valid
  private AmqpInboundProperties receive;
}
