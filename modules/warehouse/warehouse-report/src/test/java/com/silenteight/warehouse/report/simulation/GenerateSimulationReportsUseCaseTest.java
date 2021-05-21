package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinitionDto;
import com.silenteight.warehouse.common.opendistro.tenant.TenantCloningResult;
import com.silenteight.warehouse.common.opendistro.tenant.TenantService;
import com.silenteight.warehouse.indexer.analysis.AnalysisMetadataDto;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateSimulationReportsUseCaseTest {

  private static final String SOURCE_REPORT_DEF = "sourceReportId";
  private static final String TARGET_REPORT_DEF = "targetReportId";
  private static final String SOURCE_TENANT = "source_tenant";
  private static final String TARGET_TENANT = "target_tenant";
  private static final String ELASTIC_INDEX_NAME = "elastic_index_name";
  private static final String ANALYSIS = "analysis/123";

  @Mock
  private AnalysisService analysisService;

  @Mock
  private OpendistroKibanaClient opendistroKibanaClient;

  @Mock
  private TenantService tenantService;

  private GenerateSimulationReportsUseCase underTest;

  @BeforeEach
  void init() {
    underTest = new GenerateSimulationReportsUseCase(
        analysisService, opendistroKibanaClient, tenantService, SOURCE_TENANT, 1, 1);

    when(analysisService.getAnalysisMetadata(ANALYSIS)).thenReturn(getAnalysisMetadataDto());
    when(tenantService.cloneTenant(any())).thenReturn(getTenantCloningResult());
  }

  @Test
  void shouldTriggerReportGeneration() {
    when(opendistroKibanaClient.listReportDefinitions(TARGET_TENANT)).thenReturn(
        List.of(getReportDefinitionDto(TARGET_REPORT_DEF)));

    underTest.activate(ANALYSIS);

    verify(opendistroKibanaClient, atLeast(1)).createReportInstance(
        TARGET_TENANT, TARGET_REPORT_DEF);
  }

  @Test
  void shouldThrowExceptionIfReportsAreNotGenerated() {
    when(opendistroKibanaClient.listReportDefinitions(TARGET_TENANT)).thenReturn(List.of());

    underTest.activate(ANALYSIS);

    verify(opendistroKibanaClient, never()).createReportInstance(TARGET_TENANT, TARGET_REPORT_DEF);
  }

  private AnalysisMetadataDto getAnalysisMetadataDto() {
    return AnalysisMetadataDto.builder()
        .tenant(TARGET_TENANT)
        .elasticIndexName(ELASTIC_INDEX_NAME)
        .build();
  }

  private TenantCloningResult getTenantCloningResult() {
    return TenantCloningResult.builder()
        .reportMapping(Map.of(SOURCE_REPORT_DEF, TARGET_REPORT_DEF))
        .build();
  }

  private ReportDefinitionDto getReportDefinitionDto(String id) {
    return ReportDefinitionDto.builder()
        .id(id)
        .build();
  }
}
