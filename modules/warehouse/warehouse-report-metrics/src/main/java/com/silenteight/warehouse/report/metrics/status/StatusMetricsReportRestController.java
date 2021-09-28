package com.silenteight.warehouse.report.metrics.status;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.domain.ReportState;
import com.silenteight.warehouse.report.reporting.ReportStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
public class StatusMetricsReportRestController {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String DEFINITION_ID_PARAM = "definitionId";
  private static final String ID_PARAM = "id";
  private static final String STATUS_SIMULATION_REPORT_URL =
      "/v1/analysis/{analysisId}/definitions/METRICS/{definitionId}/reports/{id}/status";

  private static final String STATUS_PRODUCTION_REPORT_URL =
      "/v1/analysis/production/definitions/METRICS/{definitionId}/reports/{id}/status";

  @NonNull
  private final MetricsReportStatusQuery reportQuery;

  @GetMapping(STATUS_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(ID_PARAM) long id) {

    log.debug("Getting simulation metrics report status, analysisId={}, reportId={}", analysisId,
        id);

    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(
        state.getReportStatus(getReportName(analysisId, definitionId, id)));
  }

  @GetMapping(STATUS_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(ID_PARAM) long id) {

    log.debug(
        "Getting production metrics report status, definitionId={}, reportId={}",
        definitionId,
        id);

    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(
        state.getReportStatus(getReportName(PRODUCTION_ANALYSIS_NAME, definitionId, id)));
  }

  private static String getReportName(String analysis, String definitionId, long id) {
    return format("analysis/%s/definitions/METRICS/%s/reports/%d", analysis, definitionId, id);
  }
}
