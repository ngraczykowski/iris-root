package com.silenteight.serp.governance.qa.retention.alert;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.qa.retention.alert")
@ConstructorBinding
class AlertExpiredProperties {

  @Valid
  private int batchSize;
}
