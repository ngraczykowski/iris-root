package com.silenteight.warehouse.report.sm.create;

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
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class CreateSimulationMetricsReportRestController {

  private static final String CREATE_REPORT_URL =
      "/v1/analysis/{analysisId}/definitions/SIMULATION_METRICS/reports";

  @NonNull
  private final CreateSimulationMetricsReportUseCase createSimulationMetricsReportUseCase;

  @PostMapping(CREATE_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<Void> createReport(@PathVariable String analysisId) {

    ReportInstanceReferenceDto reportInstance = createSimulationMetricsReportUseCase
        .activate(analysisId);

    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getGetInstanceReferenceId()))
        .build();
  }

  private String getReportLocation(long timestamp) {
    return format("reports/%d/status", timestamp);
  }
}
