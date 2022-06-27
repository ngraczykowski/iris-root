package com.silenteight.simulator.management.domain;

import lombok.NonNull;

import com.silenteight.simulator.management.progress.IndexedAlertProvider;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.silenteight.simulator.management.SimulationFixtures.MAP_WITH_ANALYSIS_NAMES_AND_UPDATE_TIME_OPTIONALS;

public class TestIndexedAlertProvider implements IndexedAlertProvider {

  @Override
  public long getAllIndexedAlertsCount(@NonNull String analysisName) {
    return 0;
  }

  @Override
  public Optional<OffsetDateTime> getUpdateOnLastIndexingMessage(
      @NonNull String analysisName) {

    return MAP_WITH_ANALYSIS_NAMES_AND_UPDATE_TIME_OPTIONALS.get(analysisName);
  }
}
