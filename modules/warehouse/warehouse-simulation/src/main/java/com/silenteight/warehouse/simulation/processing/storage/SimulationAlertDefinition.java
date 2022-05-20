package com.silenteight.warehouse.simulation.processing.storage;

import lombok.NonNull;
import lombok.Value;

import java.util.List;
import javax.annotation.Nullable;

@Value
public class SimulationAlertDefinition {

  @NonNull
  String analysisName;
  @NonNull
  String alertName;
  @NonNull
  String payload;
  @NonNull
  List<SimulationMatchDefinition> matches;
  @Nullable
  Boolean migrated;
}
