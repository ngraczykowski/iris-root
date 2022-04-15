package com.silenteight.simulator.management.progress;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.simulator.common.web.rest.RestConstants.ROOT;
import static com.silenteight.simulator.management.domain.DomainConstants.SIMULATION_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = SIMULATION_ENDPOINT_TAG)
class SimulationProgressController {

  private static final String ANALYSIS_PROGRESS_URL = "/v1/simulations/{id}/progress";

  @NonNull
  private final SimulationProgressUseCase simulationProgressUseCase;

  @GetMapping(ANALYSIS_PROGRESS_URL)
  @PreAuthorize("isAuthorized('VIEW_SIMULATION_PROGRESS')")
  public ResponseEntity<SimulationProgressDto> getAnalysisProgress(@PathVariable UUID id) {
    log.info("Getting progress for simulation={}", id);
    return ok(simulationProgressUseCase.activate(id));
  }
}
