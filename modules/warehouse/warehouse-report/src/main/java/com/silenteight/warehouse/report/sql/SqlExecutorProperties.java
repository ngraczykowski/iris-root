package com.silenteight.warehouse.report.sql;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.report")
class SqlExecutorProperties {

  @NotBlank
  private String copyCsvPattern;
}
