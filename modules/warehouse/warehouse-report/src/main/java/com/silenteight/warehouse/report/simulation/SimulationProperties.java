package com.silenteight.warehouse.report.simulation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.report.simulation")
public class SimulationProperties {

  @NotBlank
  String masterTenant;
}
