package com.silenteight.warehouse.report.billing.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CreateBillingReportRestController {

  @NonNull
  private final CreateBillingReportUseCase generateReportUseCase;

  @PostMapping("/v1/analysis/production/definitions/BILLING/{reportId}/reports")
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(
      @PathVariable("reportId") String reportId) {

    log.info("Create billing report request received, reportId={}", reportId);

    ReportInstanceReferenceDto reportInstance =
        generateReportUseCase.createProductionReport(reportId);

    log.debug("Create billing report request processed, reportId={}", reportId);
    return status(SEE_OTHER)
        .header("Location", "reports/" + reportInstance.getGetInstanceReferenceId() + "/status")
        .build();
  }
}
