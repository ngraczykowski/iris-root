package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.indexer.analysis.SimulationAnalysisService;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.KIBANA_REPORT_DEFINITION_DTO;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_DEFINITION_ID;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_DESCRIPTION;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS_ID;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.SIMULATION_TENANT;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationReportingQueryTest {

  @Mock
  private ReportingService reportingService;

  @Mock
  private SimulationAnalysisService simulationAnalysisService;

  @InjectMocks
  private SimulationReportingQuery underTest;

  @Test
  void shouldReturnTenantWrapperDto() {
    //given
    when(simulationAnalysisService.getTenantIdByAnalysis(ANALYSIS)).thenReturn(ADMIN_TENANT);

    //when
    TenantDto tenantNameWrapperByAnalysis = underTest.getTenantDtoByAnalysisId(ANALYSIS_ID);

    //then
    assertThat(tenantNameWrapperByAnalysis.getTenantName()).isEqualTo(ADMIN_TENANT);
  }

  @Test
  void shouldReturnReportDefinitions() {
    when(simulationAnalysisService.getTenantIdByAnalysis(ANALYSIS))
        .thenReturn(SIMULATION_TENANT);
    when(reportingService.listReportDefinitions(SIMULATION_TENANT))
        .thenReturn(List.of(KIBANA_REPORT_DEFINITION_DTO));

    List<ReportDefinitionDto> reportsDefinitions = underTest.getReportsDefinitions(ANALYSIS_ID);

    assertThat(reportsDefinitions).hasSize(1);
    ReportDefinitionDto dto = reportsDefinitions.get(0);

    assertThat(dto.getId()).isEqualTo(REPORT_DEFINITION_ID);
    assertThat(dto.getName()).isEqualTo(
        "analysis/" + ANALYSIS_ID + "/definitions/" + REPORT_DEFINITION_ID + "/reports");
    assertThat(dto.getTitle()).isEqualTo(REPORT_NAME);
    assertThat(dto.getDescription()).isEqualTo(REPORT_DESCRIPTION);
  }
}
