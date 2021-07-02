package com.silenteight.warehouse.indexer.alert;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.alert-mapping")
public class AlertMappingProperties {

  @NotBlank
  String countrySourceKey;
}
