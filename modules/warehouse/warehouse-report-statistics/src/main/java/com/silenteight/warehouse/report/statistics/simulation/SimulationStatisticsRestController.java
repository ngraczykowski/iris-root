package com.silenteight.warehouse.report.statistics.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.statistics.domain.dto.StatisticsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class SimulationStatisticsRestController {

  private static final String GET_SIMULATION_STATISTICS_URL =
      "/v1/analysis/{analysisId}/statistics";
  private static final String ANALYSIS_ID_PARAM = "analysisId";

  @NonNull
  private final GetSimulationStatisticsUseCase useCase;

  @GetMapping(GET_SIMULATION_STATISTICS_URL)
  @PreAuthorize("isAuthorized('LIST_SIMULATION_REPORTS')")
  public ResponseEntity<StatisticsDto> getSimulationStatistics(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId) {

    StatisticsDto statisticsDto = useCase.activate(analysisId);
    return ok().body(statisticsDto);
  }
}
