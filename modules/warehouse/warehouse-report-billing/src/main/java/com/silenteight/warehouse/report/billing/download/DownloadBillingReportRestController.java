package com.silenteight.warehouse.report.billing.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.download.dto.DownloadBillingReportDto;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty("warehouse.reports.billing")
class DownloadBillingReportRestController {

  public static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/BILLING/{id}";

  @NonNull
  private final DownloadBillingReportUseCase useCase;

  @GetMapping(DOWNLOAD_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<String> downloadReport(@PathVariable("id") Long id) {
    log.info("Download Billing report request received, reportId={}", id);
    DownloadBillingReportDto reportDto = useCase.activate(id);
    String filename = reportDto.getName();
    String data = reportDto.getContent();
    log.debug("Download Billing report request processed, reportId={}, reportName={}",
        id, filename);

    return ok()
        .header("Content-Disposition", format("attachment; filename=\"%s\"", filename))
        .header("Content-Type", "text/csv")
        .body(data);
  }
}
