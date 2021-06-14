package com.silenteight.warehouse.report.production;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class ProductionReportsRestController {

  static final String DEFINITION_ID_PARAM = "definitionId";
  static final String DEFINITIONS_COLLECTION_URL =
      "/v1/analysis/production/definitions";
  static final String DEFINITIONS_RESOURCE_URL =
      DEFINITIONS_COLLECTION_URL + "/{" + DEFINITION_ID_PARAM + "}";

  @NonNull
  private ProductionReportingQuery productionReportingQuery;

  @GetMapping(DEFINITIONS_COLLECTION_URL)
  @PreAuthorize("isAuthorized('LIST_PRODUCTION_ON_DEMAND_REPORTS')")
  public ResponseEntity<List<ReportDefinitionDto>> getProductionReportDefinitionDto(
      @RequestParam(name = "type") ReportType reportType) {

    return ok()
        .body(productionReportingQuery.getReportsDefinitions(reportType));
  }
}
