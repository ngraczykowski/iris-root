package com.silenteight.warehouse.report.reasoning.match.list;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportDefinition.toProductionReportsDefinitionDto;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
class ListAiReasoningMatchLevelReportsRestController {

  private static final String LIST_METRICS_REPORT_URL =
      "/v1/analysis/production/definitions/AI_REASONING_MATCH_LEVEL";

  @GetMapping(LIST_METRICS_REPORT_URL)
  @PreAuthorize("isAuthorized('LIST_PRODUCTION_ON_DEMAND_REPORTS')")
  public ResponseEntity<List<ReportDefinitionDto>> getProductionReportDefinitions() {
    log.debug("Getting production AI Reasoning Match Level report definitions.");
    return ok().body(toProductionReportsDefinitionDto());
  }
}
