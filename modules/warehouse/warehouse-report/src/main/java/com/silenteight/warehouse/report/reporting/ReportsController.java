package com.silenteight.warehouse.report.reporting;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class ReportsController {

  static final String TENANT = "/tenant";
  static final String REPORTS = "/reports";
  static final String BASE_ANALYSIS_URL_PREFIX = "/v1/analysis/";
  static final String ANALYSIS_ID = "{analysisId}";
  @NonNull
  private ReportingService reportingService;

  @GetMapping(BASE_ANALYSIS_URL_PREFIX + ANALYSIS_ID + REPORTS)
  @PreAuthorize("isAuthorized('LIST_SIMULATION_REPORTS')")
  public ResponseEntity<KibanaReportsList> getReportsDtoList(
      @PathVariable("analysisId") String analysisName) {

    return ok().body(reportingService.getReportDtoList(analysisName));
  }

  @GetMapping(BASE_ANALYSIS_URL_PREFIX + ANALYSIS_ID + TENANT)
  @PreAuthorize("isAuthorized('GET_SIMULATION_TENANT')")
  public ResponseEntity<TenantNameWrapper> getTenantNameWrapper(
      @PathVariable("analysisId") String analysisName) {

    return ok().body(reportingService.getTenantNameWrapperByAnalysis(analysisName));
  }
}
