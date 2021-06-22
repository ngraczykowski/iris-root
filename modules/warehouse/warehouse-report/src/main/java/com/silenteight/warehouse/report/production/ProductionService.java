package com.silenteight.warehouse.report.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

@Slf4j
@RequiredArgsConstructor
class ProductionService {

  @NonNull
  private final ReportingService reportingService;

  @NonNull
  private final UserAwareReportingService userAwareReportingService;

  @NonNull
  private final ProductionReportingQuery productionReportingQuery;

  public ReportInstanceReferenceDto createProductionReport(
      ProductionReportType reportType, String definitionId) {

    String tenantName = productionReportingQuery.getTenantName(reportType);
    ReportInstanceReferenceDto report = reportingService.createReport(definitionId, tenantName);

    log.debug("Production report scheduled for generation, "
            + "reportType={}, definitionId={}, tenantName={}, reference={}",
        reportType, definitionId, tenantName, report);

    return report;
  }

  public KibanaReportDto downloadReport(
      ProductionReportType reportType, String reportDefinitionId, Long timestamp) {

    String tenantName = productionReportingQuery.getTenantName(reportType);
    return userAwareReportingService.downloadReport(tenantName, reportDefinitionId, timestamp);
  }
}
