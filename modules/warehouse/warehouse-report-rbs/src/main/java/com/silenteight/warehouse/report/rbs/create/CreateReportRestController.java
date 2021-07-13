package com.silenteight.warehouse.report.rbs.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CreateReportRestController {

  @NonNull
  private final CreateReportUseCase generateReportUseCase;

  @PostMapping("/v1/analysis/production/definitions/RB_SCORER/{reportId}/reports")
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(
      @PathVariable("reportId") String reportId) {

    ReportInstanceReferenceDto reportInstance =
        generateReportUseCase.createProductionReport(reportId);

    return status(SEE_OTHER)
        .header("Location", "reports/" + reportInstance.getTimestamp() + "/status")
        .build();
  }
}
