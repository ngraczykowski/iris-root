package com.silenteight.warehouse.report.synchronization;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.report.synchronization")
public class SynchronizationProperties {

  @NotBlank
  private String productionTenant;
  @NotBlank
  private String cron;
}
