package com.silenteight.warehouse.simulation.processing.storage;

import lombok.NonNull;
import lombok.Value;

@Value
public class SimulationMatchDefinition {

  @NonNull
  String analysisName;
  @NonNull
  String alertName;
  @NonNull
  String matchName;
  @NonNull
  String payload;
}
