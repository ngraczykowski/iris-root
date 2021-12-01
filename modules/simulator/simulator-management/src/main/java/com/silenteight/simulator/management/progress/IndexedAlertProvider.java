package com.silenteight.simulator.management.progress;

import lombok.NonNull;

public interface IndexedAlertProvider {

  long getAllIndexedAlertsCount(@NonNull String analysisName);
}
