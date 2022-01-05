package com.silenteight.warehouse.report.billing.status;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.domain.ReportState;
import com.silenteight.warehouse.report.reporting.ReportStatus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
@ConditionalOnProperty("warehouse.reports.billing")
class StatusBillingReportRestController {

  private static final String STATUS_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/BILLING/{id}/status";

  @NonNull
  private final ReportStatusQuery reportQuery;

  @GetMapping(STATUS_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(@PathVariable("id") long id) {
    ReportState state = reportQuery.getReportGeneratingState(id);
    log.debug("Getting billing report status, reportId={}", id);
    return ResponseEntity.ok(state.getReportStatus(getReportName(id)));
  }

  private static String getReportName(long id) {
    return String.format("analysis/production/reports/BILLING/%s", id);
  }
}
