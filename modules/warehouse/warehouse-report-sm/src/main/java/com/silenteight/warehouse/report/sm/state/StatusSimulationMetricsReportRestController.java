package com.silenteight.warehouse.report.sm.state;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportStatus;
import com.silenteight.warehouse.report.sm.domain.ReportState;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
public class StatusSimulationMetricsReportRestController {

  private static final String REPORT_STATUS_URL =
      "/v1/analysis/{analysisId}/definitions/SIMULATION_METRICS/{definitionId}/reports/{id}/status";

  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String DEFINITION_ID_PARAM = "definitionId";
  private static final String ID_PARAM = "id";

  @NonNull
  private final SimulationMetricsReportStatusQuery reportQuery;

  @GetMapping(REPORT_STATUS_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(ID_PARAM) long id) {

    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(state.getReportStatus(analysisId));
  }
}
