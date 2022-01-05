package com.silenteight.warehouse.report.production;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
public class ProductionReportRestController {

  static final String PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports";

  private final ProductionReportsService productionReportsService;

  @GetMapping(PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<List<ReportTypeDto>> getAvailableProductionReports() {
    return ResponseEntity.ok(productionReportsService.getListOfReports());
  }
}
