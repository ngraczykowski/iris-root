package com.silenteight.warehouse.report.rbs.state;

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
public class ReportStatusRestController {

  @NonNull
  private final ReportStatusQuery reportQuery;

  @GetMapping("/v1/analysis/production/definitions/RB_SCORER/{definitionId}/reports/{id}/status")
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable("definitionId") String definitionId, @PathVariable("id") Long id) {
    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(state.getReportStatus(getReportName(definitionId, id)));
  }

  private String getReportName(String definitionId, long id) {
    return String.format(
        "analysis/production/definitions/RB_SCORER/%s/reports/%d", definitionId, id);
  }
}
