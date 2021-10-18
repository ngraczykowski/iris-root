package com.silenteight.warehouse.report.simulation.v1;

import lombok.Data;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.report.deprecated-simulation")
class DeprecatedSimulationProperties {

  @NonNull
  private List<String> hiddenTypes = emptyList();
}
