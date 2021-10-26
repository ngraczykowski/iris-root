package com.silenteight.warehouse.report.accuracy.status;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.accuracy.domain.ReportState;
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
class StatusAccuracyReportRestController {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String ID_PARAM = "id";
  private static final String STATUS_SIMULATION_REPORT_URL =
      "/v2/analysis/{analysisId}/reports/ACCURACY/{id}/status";

  private static final String STATUS_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/ACCURACY/{id}/status";

  @NonNull
  private final AccuracyReportStatusQuery reportQuery;

  @GetMapping(STATUS_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(ID_PARAM) long id) {

    log.debug("Getting simulation Accuracy report status, analysisId={}, reportId={}",
        analysisId, id);

    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(state.getReportStatus(getReportName(analysisId, id)));
  }

  @GetMapping(STATUS_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(@PathVariable(ID_PARAM) long id) {
    log.debug("Getting production Accuracy report status, reportId={}", id);
    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(state.getReportStatus(getReportName(PRODUCTION_ANALYSIS_NAME, id)));
  }

  private static String getReportName(String analysis, long id) {
    return format("analysis/%s/reports/ACCURACY/%d", analysis, id);
  }
}
