package com.silenteight.warehouse.report.billing.download;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.download.dto.DownloadBillingReportDto;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ROOT)
class DownloadBillingReportRestController {

  public static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/BILLING/{id}";

  @Nullable
  private final DownloadBillingReportUseCase useCase;


  @GetMapping(DOWNLOAD_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<String> downloadReport(@PathVariable("id") Long id) {
    log.info("Download Billing report request received, reportId={}", id);
    if (Objects.isNull(useCase)) {
      return status(NOT_FOUND).build();
    }
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
