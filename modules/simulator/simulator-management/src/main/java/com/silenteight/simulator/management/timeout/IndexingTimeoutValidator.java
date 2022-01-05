package com.silenteight.simulator.management.timeout;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.simulator.management.domain.dto.SimulationDto;
import com.silenteight.simulator.management.progress.IndexedAlertProvider;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

import static com.silenteight.simulator.management.timeout.TimeValidator.isTimeoutOnTime;

@RequiredArgsConstructor
class IndexingTimeoutValidator extends BaseSimulationTimeoutValidator {

  private static final String TIMEOUT_MESSAGE =
      "Timeout on indexing validation for simulationId={}";

  private static final String NO_TIMEOUT_MESSAGE =
      "No timeout on indexing validation for simulationId={}";

  @NonNull
  private final TimeSource timeSource;

  @NonNull
  private final IndexedAlertProvider indexedAlertProvider;

  @NonNull
  private final Duration durationTime;

  @Override
  public boolean valid(SimulationDto simulationDto) {
    boolean result = timeoutOnIndexingProgress(simulationDto);
    doLog(result);
    return result;
  }

  private boolean timeoutOnIndexingProgress(SimulationDto simulationDto) {
    String analysisName = simulationDto.getAnalysis();
    OffsetDateTime timeoutPoint = getTimeoutPoint();
    Optional<OffsetDateTime> lastUpdateOnIndexing =
        indexedAlertProvider.getUpdateOnLastIndexingMessage(analysisName);

    if (lastUpdateOnIndexing.isEmpty()) {
      return isTimeoutOnTime(simulationDto.getUpdatedAt(), timeoutPoint);
    } else {
      return isTimeoutOnTime(lastUpdateOnIndexing.get(), timeoutPoint);
    }
  }

  private OffsetDateTime getTimeoutPoint() {
    OffsetDateTime dateTime = timeSource.offsetDateTime();
    return dateTime.minus(durationTime);
  }

  @Override
  protected String getTimeoutMessage() {
    return TIMEOUT_MESSAGE;
  }

  @Override
  protected String getNoTimeoutMessage() {
    return NO_TIMEOUT_MESSAGE;
  }
}
