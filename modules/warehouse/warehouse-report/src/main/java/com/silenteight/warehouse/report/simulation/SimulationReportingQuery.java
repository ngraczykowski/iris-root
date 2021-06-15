package com.silenteight.warehouse.report.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDefinitionDto;
import com.silenteight.warehouse.indexer.analysis.SimulationAnalysisService;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.report.reporting.AnalysisResource.toResourceName;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.ANALYSIS_ID_PARAM;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.DEFINITIONS_RESOURCE_NAME;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.DEFINITION_ID_PARAM;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RequiredArgsConstructor
public class SimulationReportingQuery {

  @NonNull
  private final ReportingService reportingService;

  @NonNull
  private final SimulationAnalysisService simulationAnalysisService;

  List<ReportDefinitionDto> getReportsDefinitions(String analysisId) {
    String tenantName = getTenantIdByAnalysisId(analysisId);

    return reportingService.listReportDefinitions(tenantName)
        .stream()
        .map(reportDefinitionDto -> convertToDefinitionDto(reportDefinitionDto, analysisId))
        .collect(toList());
  }

  public String getTenantIdByAnalysisId(String analysisId) {
    return simulationAnalysisService.getTenantIdByAnalysis(toResourceName(analysisId));
  }

  public TenantDto getTenantDtoByAnalysisId(String analysisId) {
    String tenantIdByAnalysisId = getTenantIdByAnalysisId(analysisId);
    return TenantDto.builder()
        .tenantName(tenantIdByAnalysisId)
        .build();
  }

  private static ReportDefinitionDto convertToDefinitionDto(
      KibanaReportDefinitionDto reportDefinitionDto, String analysisId) {

    return ReportDefinitionDto.builder()
        .id(reportDefinitionDto.getId())
        .name(buildName(analysisId, reportDefinitionDto.getId()))
        .description(reportDefinitionDto.getDescription())
        .title(reportDefinitionDto.getReportName())
        .build();
  }

  private static String buildName(String analysisId, String reportDefinitionId) {
    return fromPath(DEFINITIONS_RESOURCE_NAME)
        .buildAndExpand(Map.of(
            ANALYSIS_ID_PARAM, analysisId,
            DEFINITION_ID_PARAM, reportDefinitionId))
        .toUriString();
  }
}
