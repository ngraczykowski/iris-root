package com.silenteight.warehouse.report.rbs.status;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.ReportState;
import com.silenteight.warehouse.report.reporting.ReportStatus;

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
class StatusRbsReportRestController {

  @NonNull
  private final RbsReportStatusQuery reportQuery;

  @GetMapping("/v1/analysis/production/definitions/RB_SCORER/{definitionId}/reports/{id}/status")
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable("definitionId") String definitionId, @PathVariable("id") Long id) {
    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(state.getReportStatus(getReportName("production", definitionId, id)));
  }

  @GetMapping("/v1/analysis/{analysisId}/definitions/RB_SCORER/{definitionId}/reports/{id}/status")
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable("analysisId") String analysisId,
      @PathVariable("definitionId") String definitionId,
      @PathVariable("id") Long id) {
    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(state.getReportStatus(getReportName(analysisId, definitionId, id)));
  }

  private String getReportName(String analysis, String definitionId, long id) {
    return String.format(
        "analysis/%s/definitions/RB_SCORER/%s/reports/%d", analysis, definitionId, id);
  }
}
