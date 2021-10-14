package com.silenteight.warehouse.report.simulation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
@Slf4j
class SimulationReportsRestController {

  static final String ANALYSIS_ID_PARAM = "analysisId";
  static final String DEFINITIONS_COLLECTION_URL = "/v2/analysis/{analysisId}/reports";

  @NonNull
  private SimulationReportsUseCase reportsDefinitionsUseCase;

  @GetMapping(DEFINITIONS_COLLECTION_URL)
  @PreAuthorize("isAuthorized('LIST_SIMULATION_REPORTS')")
  public ResponseEntity<List<ReportTypeDto>> listTypes(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId) {

    log.debug("Getting report definition list for analysisId={}", analysisId);
    return ok().body(reportsDefinitionsUseCase.activate(analysisId));
  }
}
