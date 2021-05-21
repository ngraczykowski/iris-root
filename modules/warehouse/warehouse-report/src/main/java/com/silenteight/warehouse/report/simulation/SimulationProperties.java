package com.silenteight.warehouse.report.simulation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.report.simulation")
public class SimulationProperties {

  @NotBlank
  String masterTenant;
  @NotNull
  Integer pollingIntervalMs;
  @NotNull
  Integer pollingMaxAttemptCount;
}
