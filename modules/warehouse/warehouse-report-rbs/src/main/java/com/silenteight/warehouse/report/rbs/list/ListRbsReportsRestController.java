package com.silenteight.warehouse.report.rbs.list;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.rbs.domain.ReportDefinition;
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
class ListRbsReportsRestController {

  @GetMapping("/v1/analysis/production/definitions/RB_SCORER")
  @PreAuthorize("isAuthorized('LIST_PRODUCTION_ON_DEMAND_REPORTS')")
  public ResponseEntity<List<ReportDefinitionDto>> getProductionReportDefinitions() {
    log.debug("Getting production report definitions.");
    return ok().body(ReportDefinition.toReportsDefinitionDto());
  }
}
