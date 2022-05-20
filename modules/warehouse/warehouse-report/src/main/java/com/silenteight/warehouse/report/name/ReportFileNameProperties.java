package com.silenteight.warehouse.report.name;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.report.file-name")
class ReportFileNameProperties {

  @NotBlank
  private String productionPattern;
  @NotBlank
  private String simulationPattern;
}
