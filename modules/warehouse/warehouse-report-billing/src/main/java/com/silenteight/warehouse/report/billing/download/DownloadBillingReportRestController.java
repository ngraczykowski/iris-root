package com.silenteight.warehouse.report.billing.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.billing.domain.dto.ReportDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class DownloadBillingReportRestController {

  @NonNull
  private final ReportDataQuery reportDataQuery;
  @NonNull
  private final BillingReportService reportService;

  @GetMapping("/v1/analysis/production/definitions/BILLING/{definitionId}/reports/{id}")
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<String> downloadReport(
      @PathVariable("definitionId") String definitionId,
      @PathVariable("id") Long id) {

    ReportDto reportDto = reportDataQuery.getReport(id);
    String filename = reportDto.getFilename();
    String data = reportDto.getContent();

    reportService.removeReport(id);
    log.debug("Billing report removed, reportId={},id");

    return ok()
        .header("Content-Disposition", format("attachment; filename=\"%s\"", filename))
        .header("Content-Type", "text/csv")
        .body(data);
  }
}
