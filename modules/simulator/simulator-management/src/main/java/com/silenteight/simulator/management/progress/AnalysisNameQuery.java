package com.silenteight.simulator.management.progress;

import java.util.UUID;

public interface AnalysisNameQuery {

  String getAnalysisName(UUID simulationId);
}
