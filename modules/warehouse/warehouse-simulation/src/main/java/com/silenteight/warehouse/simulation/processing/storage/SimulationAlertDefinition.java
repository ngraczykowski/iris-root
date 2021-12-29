package com.silenteight.warehouse.simulation.processing.storage;

import lombok.NonNull;
import lombok.Value;

@Value
public class SimulationAlertDefinition {

  @NonNull
  String analysisName;
  @NonNull
  String alertName;
  @NonNull
  String payload;
}
