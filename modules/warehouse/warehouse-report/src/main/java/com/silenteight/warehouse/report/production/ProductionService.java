package com.silenteight.warehouse.report.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

@RequiredArgsConstructor
class ProductionService {

  @NonNull
  private final ReportingService reportingService;

  @NonNull
  private final UserAwareReportingService userAwareReportingService;

  @NonNull
  private final ProductionReportingQuery productionReportingQuery;

  public ReportInstanceReferenceDto createSimulationReport(
      ProductionReportType reportType, String definitionId) {

    String tenantName = productionReportingQuery.getTenantName(reportType);
    return reportingService.createReport(definitionId, tenantName);
  }

  public KibanaReportDto downloadReport(
      ProductionReportType reportType, String reportDefinitionId, Long timestamp) {

    String tenantName = productionReportingQuery.getTenantName(reportType);
    return userAwareReportingService.downloadReport(tenantName, reportDefinitionId, timestamp);
  }
}
