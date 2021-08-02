package com.silenteight.warehouse.indexer.query.grouping;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.data.production.status")
public class DataProductionProperties {

  @NotBlank
  private String fieldName;
  @NotBlank
  private String completedValue;
}
