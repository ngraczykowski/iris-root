package com.silenteight.warehouse.report.production;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.warehouse.report.reporting.ReportType;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class ProductionReportsController {

  static final String PRODUCTION_REPORT_DEFINITIONS_LIST_URL =
      "/v1/analysis/production/definitions";

  @NonNull
  private ProductionReportingQuery productionReportingQuery;

  @GetMapping(PRODUCTION_REPORT_DEFINITIONS_LIST_URL)
  @PreAuthorize("isAuthorized('LIST_PRODUCTION_ON_DEMAND_REPORTS')")
  public ResponseEntity<ReportsDefinitionListDto> getProductionReportDefinitionDto(
      @RequestParam(name = "type") ReportType reportType) {

    return ok()
        .body(productionReportingQuery.getReportsDefinitions(reportType));
  }
}
