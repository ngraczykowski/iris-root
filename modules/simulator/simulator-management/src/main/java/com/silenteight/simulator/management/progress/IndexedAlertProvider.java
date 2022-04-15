package com.silenteight.simulator.management.progress;

import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface IndexedAlertProvider {

  long getAllIndexedAlertsCount(@NonNull String analysisName);

  Optional<OffsetDateTime> getUpdateOnLastIndexingMessage(@NonNull String analysisName);
}
