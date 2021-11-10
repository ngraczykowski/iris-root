package com.silenteight.warehouse.retention.simulation;

import lombok.Data;
import lombok.NonNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.retention.simulation.analysis-expired")
@ConstructorBinding
class RetentionSimulationProperties {

  @NonNull
  List<String> fieldsToErase;
}
