package com.silenteight.simulator.management.timeout;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.management.domain.dto.SimulationDto;
import com.silenteight.simulator.management.list.ListSimulationsQuery;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.silenteight.simulator.management.domain.SimulationState.RUNNING;
import static com.silenteight.simulator.management.domain.SimulationState.STREAMING;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class SimulationTimeoutService {

  @NonNull
  private final ListSimulationsQuery listSimulationsQuery;

  @NonNull
  private final SimulationService simulationService;

  @NonNull
  private final List<SimulationTimeoutValidator> validators;

  @NonNull
  private final SimulationLastCheckTimes simulationLastCheckTimes;

  @Transactional
  public void timeoutSimulations() {
    List<SimulationDto> runningSimulations = listSimulationsQuery
        .listDomainDto(of(RUNNING, STREAMING))
        .stream()
        .filter(simulationDto -> simulationLastCheckTimes
            .isIntervalElapsed(simulationDto.getSimulationName()))
        .collect(toList());

    runningSimulations.forEach(this::timeoutSingleSimulation);
  }

  private void timeoutSingleSimulation(SimulationDto simulationDto) {
    log.info("Process of checking timeout for simulationId={} started.", simulationDto.getId());
    simulationLastCheckTimes.updateSimulationCheckTimestamp(simulationDto.getSimulationName());

    boolean doTimeout = validators.stream().anyMatch(validator -> validator.valid(simulationDto));
    if (doTimeout)
      simulationService.timeout(simulationDto.getId());
  }
}
