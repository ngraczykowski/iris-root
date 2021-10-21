package com.silenteight.warehouse.report.rbs.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CreateRbsReportRestController {

  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String CREATE_SIMULATION_REPORT_URL =
      "/v2/analysis/{analysisId}/reports/RB_SCORER";

  private static final String CREATE_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/RB_SCORER";

  @NonNull
  private final CreateProductionRbsReportUseCase createProductionRbsReportUseCase;
  @NonNull
  private final CreateSimulationRbsReportUseCase createSimulationRbsReportUseCase;

  @PostMapping(CREATE_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(
      @DateTimeFormat(iso = DATE)
      @RequestParam LocalDate from,
      @DateTimeFormat(iso = DATE)
      @RequestParam LocalDate to) {

    log.info("Create production RB Scorer report request received, from={} - to={}", from, to);
    ReportInstanceReferenceDto reportInstance =
        createProductionRbsReportUseCase.createReport(from, to);

    log.debug("Create production RB Scorer report request processed, from={} - to={}, reportId={}",
        from, to, reportInstance.getInstanceReferenceId());

    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getInstanceReferenceId()))
        .build();
  }

  @PostMapping(CREATE_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<Void> createReport(@PathVariable(ANALYSIS_ID_PARAM) String analysisId) {
    log.info("Create simulation RB Scorer report request received,analysisId={}", analysisId);
    ReportInstanceReferenceDto reportInstance =
        createSimulationRbsReportUseCase.createReport(analysisId);

    log.debug("Create simulation RB Scorer report request processed,analysisId={}", analysisId);
    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getInstanceReferenceId()))
        .build();
  }

  private static String getReportLocation(long id) {
    return format("RB_SCORER/%d/status", id);
  }
}
