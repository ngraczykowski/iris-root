package com.silenteight.warehouse.report.generation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "warehouse.report.zip")
public class ReportZipProperties {

  private boolean enabled;
  private int rowsLimit;
}
