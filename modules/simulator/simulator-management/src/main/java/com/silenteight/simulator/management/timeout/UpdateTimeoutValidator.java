package com.silenteight.simulator.management.timeout;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.simulator.management.domain.dto.SimulationDto;

import java.time.Duration;
import java.time.OffsetDateTime;

import static com.silenteight.simulator.management.timeout.TimeValidator.isTimeoutOnTime;

@RequiredArgsConstructor
class UpdateTimeoutValidator extends BaseSimulationTimeoutValidator {

  private static final String TIMEOUT_MESSAGE = "Timeout on update time of simulation";
  private static final String NO_TIMEOUT_MESSAGE = "No timeout on update time of simulation.";

  @NonNull
  private final TimeSource timeSource;

  @NonNull
  private final Duration durationTime;

  @Override
  public boolean valid(SimulationDto simulationDto) {
    boolean result = checkLastUpdateTime(simulationDto);
    doLog(result);
    return result;
  }

  private boolean checkLastUpdateTime(SimulationDto simulationDto) {
    OffsetDateTime updatedAt = simulationDto.getUpdatedAt();
    OffsetDateTime timeoutPoint = getTimeoutPoint();
    return isTimeoutOnTime(updatedAt, timeoutPoint);
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
