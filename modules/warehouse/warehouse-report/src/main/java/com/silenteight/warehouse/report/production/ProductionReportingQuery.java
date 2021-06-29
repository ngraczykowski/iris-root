package com.silenteight.warehouse.report.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDefinitionDto;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.production.ProductionReportsRestController.DEFINITIONS_RESOURCE_NAME;
import static com.silenteight.warehouse.report.production.ProductionReportsRestController.DEFINITION_ID_PARAM;
import static com.silenteight.warehouse.report.production.ProductionReportsRestController.REPORT_TYPE_PARAM;
import static java.util.Map.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RequiredArgsConstructor
class ProductionReportingQuery {

  @NonNull
  @Valid
  private final EnvironmentProperties environmentProperties;
  @NonNull
  private final ReportingService reportingService;

  List<ReportDefinitionDto> getReportsDefinitions(ProductionReportType reportType) {
    String tenantName = getTenantName(reportType);

    return reportingService.listReportDefinitions(tenantName).stream()
        .map(report -> convertToDefinitionDto(report, reportType))
        .collect(toList());
  }

  String getTenantName(ProductionReportType reportType) {
    return reportType.getTenantName(environmentProperties.getPrefix());
  }

  private static ReportDefinitionDto convertToDefinitionDto(
      KibanaReportDefinitionDto reportDefinitionDto, ProductionReportType reportType) {

    String type = reportType.toString();
    String title = reportType.getTitle();

    return ReportDefinitionDto.builder()
        .id(reportDefinitionDto.getId())
        .name(buildName(reportDefinitionDto.getId(), reportType))
        .title(title)
        .description(reportDefinitionDto.getDescription())
        .reportType(type)
        .build();
  }

  private static String buildName(String reportDefinitionId, ProductionReportType reportType) {
    return fromPath(DEFINITIONS_RESOURCE_NAME)
        .buildAndExpand(of(
            DEFINITION_ID_PARAM, reportDefinitionId,
            REPORT_TYPE_PARAM, reportType.toString()))
        .toUriString();
  }
}
