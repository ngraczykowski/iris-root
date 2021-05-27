package com.silenteight.warehouse.report.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.report.reporting.ReportType;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto;

import javax.validation.Valid;

@RequiredArgsConstructor
class ProductionReportingQuery {

  static final String REPORT_DEFINITION_NAME_PREFIX = "/analysis/production/definitions/";
  static final String FLOW_PROD = "production";

  @NonNull
  @Valid
  private final EnvironmentProperties environmentProperties;
  @NonNull
  private final ReportingService reportingService;

  ReportsDefinitionListDto getReportsDefinitions(ReportType reportType) {
    String tenantName = getTenantName(reportType, environmentProperties.getPrefix());
    return reportingService.getReportDefinitionsByTenant(
        reportType.toString(), tenantName, REPORT_DEFINITION_NAME_PREFIX);
  }

  private String getTenantName(ReportType reportType, String env) {
    return ReportType.valueOf(reportType.toString()).getTenantName(env, FLOW_PROD);
  }
}
