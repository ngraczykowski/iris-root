package com.silenteight.warehouse.report.reasoning.match.v1.create;

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
class DeprecatedCreateAiReasoningMatchLevelReportRestController {

  private static final String CREATE_PRODUCTION_REPORT_URL =
      "/v1/analysis/production/definitions/AI_REASONING_MATCH_LEVEL/{id}/reports";

  @NonNull
  private final DeprecatedCreateProductionAiReasoningMatchLevelReportUseCase
      createProductionReportUseCase;

  @PostMapping(CREATE_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(@PathVariable("id") String id) {

    log.info("Create production AI Reasoning Match Level report request received, reportId={}", id);

    ReportInstanceReferenceDto reportInstance =
        createProductionReportUseCase.createReport(id);

    log.debug("Create production AI Reasoning Match Level report request processed, reportId={}",
        id);

    return status(SEE_OTHER)
        .header("Location", getReportLocation(reportInstance.getInstanceReferenceId()))
        .build();
  }

  private static String getReportLocation(long timestamp) {
    return format("reports/%d/status", timestamp);
  }
}
