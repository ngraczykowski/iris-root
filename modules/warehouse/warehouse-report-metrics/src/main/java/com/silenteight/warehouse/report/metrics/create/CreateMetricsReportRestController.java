package com.silenteight.warehouse.report.metrics.create;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SEE_OTHER;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ROOT)
class CreateMetricsReportRestController {

  private static final String CREATE_SIMULATION_REPORT_URL =
      "/v2/analysis/{analysisId}/reports/METRICS";

  private static final String CREATE_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/METRICS";

  @Nullable
  private final CreateProductionMetricsReportUseCase createProductionMetricsReportUseCase;

  @Nullable
  private final CreateSimulationMetricsReportUseCase createSimulationMetricsReportUseCase;

  @PostMapping(CREATE_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<Void> createReport(
      @PathVariable String analysisId) {

    log.info("Create simulation metrics report request received, analysisId={}", analysisId);

    if (Objects.isNull(createSimulationMetricsReportUseCase)) {
      return ResponseEntity.status(NOT_FOUND).build();
    }

    ReportInstanceReferenceDto reportInstance = createSimulationMetricsReportUseCase
        .createReport(analysisId);

    log.debug(
        "Create simulation metrics report request processed, analysisId={}, reportId={}",
        analysisId, reportInstance.getInstanceReferenceId());

    return ResponseEntity.status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getInstanceReferenceId()))
        .build();
  }

  @PostMapping(CREATE_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(
      @DateTimeFormat(iso = DATE_TIME)
      @RequestParam OffsetDateTime from,
      @DateTimeFormat(iso = DATE_TIME)
      @RequestParam OffsetDateTime to) {

    log.info("Create production Metrics report request received, from={} - to={}", from, to);

    if (Objects.isNull(createProductionMetricsReportUseCase)) {
      return ResponseEntity.status(NOT_FOUND).build();
    }

    ReportInstanceReferenceDto reportInstance =
        createProductionMetricsReportUseCase.createReport(from, to);

    log.debug("Create production Metrics report request processed, from={} - to={}, reportId={}",
        from, to, reportInstance.getInstanceReferenceId());

    return ResponseEntity.status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getInstanceReferenceId()))
        .build();
  }

  private static String getReportLocation(long id) {
    return format("METRICS/%d/status", id);
  }
}
