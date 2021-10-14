package com.silenteight.warehouse.report.metrics.v1.create;

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
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class DeprecatedCreateMetricsReportRestController {

  private static final String CREATE_SIMULATION_REPORT_URL =
      "/v1/analysis/{analysisId}/definitions/METRICS/{id}/reports";

  private static final String CREATE_PRODUCTION_REPORT_URL =
      "/v1/analysis/production/definitions/METRICS/{id}/reports";

  @NonNull
  private final DeprecatedCreateMetricsReportUseCase deprecatedCreateMetricsReportUseCase;

  @PostMapping(CREATE_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<Void> createReport(
      @PathVariable String analysisId,
      @PathVariable String id) {

    log.info("Create simulation metrics report request received, analysisId={},id={}", analysisId,
        id);

    ReportInstanceReferenceDto reportInstance = deprecatedCreateMetricsReportUseCase
        .createSimulationReport(analysisId);

    log.debug(
        "Create simulation metrics report request processed, analysisId={},id={}, reportId={}",
        analysisId, id, reportInstance.getInstanceReferenceId());

    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getInstanceReferenceId()))
        .build();
  }

  @PostMapping(CREATE_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(@PathVariable("id") String id) {

    log.info("Create production Metrics report request received, reportId={}", id);

    ReportInstanceReferenceDto reportInstance =
        deprecatedCreateMetricsReportUseCase.createProductionReport(id);

    log.debug("Create production Metrics report request processed, reportId={}", id);
    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getInstanceReferenceId()))
        .build();
  }

  private static String getReportLocation(long timestamp) {
    return format("reports/%d/status", timestamp);
  }
}
