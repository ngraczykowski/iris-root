package com.silenteight.warehouse.report.reasoning.match.status;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reasoning.match.domain.ReportState;
import com.silenteight.warehouse.report.reporting.ReportStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class StatusAiReasoningMatchLevelReportRestController {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String DEFINITION_ID_PARAM = "definitionId";
  private static final String ID_PARAM = "id";

  private static final String STATUS_PRODUCTION_REPORT_URL =
      "/v1/analysis/production/definitions/AI_REASONING_MATCH_LEVEL/"
          + "{definitionId}/reports/{id}/status";

  @NonNull
  private final AiReasoningMatchLevelReportStatusQuery reportQuery;

  @GetMapping(STATUS_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(ID_PARAM) long id) {

    log.debug("Getting production AI Reasoning Match Level report status, definitionId={}, "
        + "reportId={}", definitionId, id);

    ReportState state = reportQuery.getReportGeneratingState(id);
    return ResponseEntity.ok(
        state.getReportStatus(getReportName(PRODUCTION_ANALYSIS_NAME, definitionId, id)));
  }

  private static String getReportName(String analysis, String definitionId, long id) {
    return format("analysis/%s/definitions/AI_REASONING_MATCH_LEVEL/%s/reports/%d",
        analysis, definitionId, id);
  }
}
