package com.silenteight.warehouse.retention.production.alert;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.retention.alert")
@ConstructorBinding
class EraseAlertsProperties {

  @Valid
  private final int batchSize;
}
