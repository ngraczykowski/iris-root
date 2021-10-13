package com.silenteight.warehouse.report.metrics.list;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.domain.DeprecatedMetricsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
class DeprecatedListMetricsReportsRestController {

  private static final String LIST_METRICS_REPORT_URL =
      "/v1/analysis/production/definitions/METRICS";

  @GetMapping(LIST_METRICS_REPORT_URL)
  @PreAuthorize("isAuthorized('LIST_PRODUCTION_ON_DEMAND_REPORTS')")
  public ResponseEntity<List<ReportDefinitionDto>> getProductionReportDefinitions() {
    log.debug("Getting production metrics report definitions.");
    return ok().body(DeprecatedMetricsReportDefinition.toProductionReportsDefinitionDto());
  }
}
