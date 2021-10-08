package com.silenteight.warehouse.report.reasoning.create;

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
class CreateAiReasoningReportRestController {

  private static final String CREATE_SIMULATION_REPORT_URL =
      "/v1/analysis/{analysisId}/definitions/AI_REASONING/{id}/reports";

  private static final String CREATE_PRODUCTION_REPORT_URL =
      "/v1/analysis/production/definitions/AI_REASONING/{id}/reports";

  @NonNull
  private final CreateProductionAiReasoningReportUseCase createProductionReportUseCase;
  @NonNull
  private final CreateSimulationAiReasoningReportUseCase createSimulationReportUseCase;

  @PostMapping(CREATE_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<Void> createReport(
      @PathVariable String analysisId,
      @PathVariable String id) {

    log.info(
        "Create simulation AI Reasoning report request received, analysisId={},id={}",
        analysisId,
        id);

    ReportInstanceReferenceDto reportInstance = createSimulationReportUseCase
        .createReport(analysisId);

    log.debug(
        "Create simulation AI Reasoning report request processed, "
            + "analysisId={}, id={}, reportId={}",
        analysisId, id, reportInstance.getGetInstanceReferenceId());

    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getGetInstanceReferenceId()))
        .build();
  }

  @PostMapping(CREATE_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(@PathVariable("id") String id) {

    log.info("Create production AI Reasoning report request received, reportId={}", id);

    ReportInstanceReferenceDto reportInstance =
        createProductionReportUseCase.createReport(id);

    log.debug("Create production AI Reasoning report request processed, reportId={}", id);
    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getGetInstanceReferenceId()))
        .build();
  }

  private static String getReportLocation(long timestamp) {
    return format("reports/%d/status", timestamp);
  }
}
