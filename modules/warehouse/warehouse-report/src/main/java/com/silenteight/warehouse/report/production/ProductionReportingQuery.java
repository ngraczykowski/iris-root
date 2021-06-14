package com.silenteight.warehouse.report.production;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDefinitionDto;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.production.ProductionReportsRestController.DEFINITIONS_RESOURCE_URL;
import static com.silenteight.warehouse.report.production.ProductionReportsRestController.DEFINITION_ID_PARAM;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RequiredArgsConstructor
class ProductionReportingQuery {

  static final String FLOW_PROD = "production";

  @NonNull
  @Valid
  private final EnvironmentProperties environmentProperties;
  @NonNull
  private final ReportingService reportingService;

  List<ReportDefinitionDto> getReportsDefinitions(ReportType reportType) {
    String tenantName = getTenantName(reportType, environmentProperties.getPrefix());

    return reportingService.listReportDefinitions(tenantName).stream()
        .map(report -> convertToDefinitionDto(report, reportType))
        .collect(toList());
  }

  private String getTenantName(ReportType reportType, String env) {
    return reportType.getTenantName(env, FLOW_PROD);
  }

  private static ReportDefinitionDto convertToDefinitionDto(
      KibanaReportDefinitionDto reportDefinitionDto, ReportType reportType) {

    String type = reportType.toString();
    String title = reportType.getTitle();

    return ReportDefinitionDto.builder()
        .id(reportDefinitionDto.getId())
        .name(buildName(reportDefinitionDto.getId()))
        .title(title)
        .description(reportDefinitionDto.getDescription())
        .reportType(type)
        .build();
  }

  private static String buildName(String reportDefinitionId) {
    return fromPath(DEFINITIONS_RESOURCE_URL)
        .buildAndExpand(Map.of(
            DEFINITION_ID_PARAM, reportDefinitionId))
        .toUriString();
  }
}
