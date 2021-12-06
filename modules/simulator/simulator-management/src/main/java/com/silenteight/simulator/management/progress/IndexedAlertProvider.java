package com.silenteight.simulator.management.progress;

import lombok.NonNull;

import java.util.Optional;

public interface IndexedAlertProvider {

  Optional<Long> getAllIndexedAlertsCount(@NonNull String analysisName);
}
