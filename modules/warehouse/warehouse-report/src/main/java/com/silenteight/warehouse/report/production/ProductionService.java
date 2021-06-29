package com.silenteight.warehouse.report.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportStatus;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

import static com.silenteight.warehouse.report.production.ProductionReportsRestController.DEFINITION_ID_PARAM;
import static com.silenteight.warehouse.report.production.ProductionReportsRestController.REPORTS_RESOURCE_URL;
import static com.silenteight.warehouse.report.production.ProductionReportsRestController.REPORT_TYPE_PARAM;
import static com.silenteight.warehouse.report.production.ProductionReportsRestController.TIMESTAMP_PARAM;
import static com.silenteight.warehouse.report.reporting.ReportStatus.buildReportStatusGenerating;
import static com.silenteight.warehouse.report.reporting.ReportStatus.buildReportStatusOk;
import static java.util.Map.of;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

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

    String tenantName = getTenantName(reportType);
    ReportInstanceReferenceDto report = reportingService.createReport(definitionId, tenantName);

    log.debug("Production report scheduled for generation, "
            + "reportType={}, definitionId={}, tenantName={}, reference={}",
        reportType, definitionId, tenantName, report);

    return report;
  }

  public KibanaReportDto downloadReport(
      ProductionReportType reportType, String reportDefinitionId, Long timestamp) {

    String tenantName = getTenantName(reportType);
    return userAwareReportingService.downloadReport(tenantName, reportDefinitionId, timestamp);
  }

  public ReportStatus getReportGeneratingStatus(
      ProductionReportType reportType, String definitionId, Long timestamp) {

    String tenantName = getTenantName(reportType);
    String downloadReportUrl = buildDownloadReportUrl(reportType, definitionId, timestamp);

    return userAwareReportingService.getReportInstanceId(tenantName, definitionId, timestamp)
        .map(id -> buildReportStatusOk(downloadReportUrl))
        .orElse(buildReportStatusGenerating());
  }

  private String getTenantName(ProductionReportType reportType) {
    return productionReportingQuery.getTenantName(reportType);
  }

  private String buildDownloadReportUrl(
      ProductionReportType reportType, String definitionId, Long timestamp) {

    return fromPath(REPORTS_RESOURCE_URL)
        .buildAndExpand(of(REPORT_TYPE_PARAM, reportType, DEFINITION_ID_PARAM, definitionId,
            TIMESTAMP_PARAM, timestamp))
        .toUriString();
  }
}
