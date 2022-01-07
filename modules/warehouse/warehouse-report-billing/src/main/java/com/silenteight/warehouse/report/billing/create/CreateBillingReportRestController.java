package com.silenteight.warehouse.report.billing.create;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class CreateBillingReportRestController {

  private static final String CREATE_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/BILLING";

  @Nullable
  private final CreateBillingReportUseCase generateReportUseCase;

  @PostMapping(CREATE_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(
      @DateTimeFormat(iso = DATE_TIME)
      @RequestParam OffsetDateTime from,
      @DateTimeFormat(iso = DATE_TIME)
      @RequestParam OffsetDateTime to) {

    log.info("Create Billing report request received, from={} - to={}", from, to);

    if (Objects.isNull(generateReportUseCase)) {
      return status(NOT_FOUND).build();
    }

    ReportInstanceReferenceDto reportInstance =
        generateReportUseCase.createReport(from, to);

    log.debug("Create billing report request processed, from={} - to={}, reportId={}",
        from, to, reportInstance.getInstanceReferenceId());

    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getInstanceReferenceId()))
        .build();
  }

  private static String getReportLocation(long id) {
    return format("BILLING/%d/status", id);
  }
}
