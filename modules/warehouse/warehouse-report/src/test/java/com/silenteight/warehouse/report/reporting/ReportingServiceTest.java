package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDefinitionDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;
import com.silenteight.warehouse.indexer.analysis.AnalysisService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static com.silenteight.warehouse.report.reporting.AnalysisResource.ANALYSIS_PREFIX;
import static com.silenteight.warehouse.report.reporting.ReportServiceTestConstants.*;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

  @Mock
  private OpendistroElasticClient opendistroElasticClient;
  @Mock
  private OpendistroKibanaClient opendistroKibanaClient;

  @Mock
  private AnalysisService analysisService;

  private ReportingService underTest;

  @BeforeEach
  void setUp() {
    underTest = new ReportingService(
        opendistroElasticClient, opendistroKibanaClient, analysisService);
  }

  @Test
  void shouldReturnReportDto() {
    when(analysisService.getTenantIdByAnalysis(ANALYSIS_PREFIX + ANALYSIS_NAME))
        .thenReturn(ADMIN_TENANT);
    when(opendistroElasticClient.getReportInstances(any()))
        .thenReturn(LIST_REPORTS_INSTANCES_RESPONSE);

    KibanaReportsList reportDtoList = underTest.getReportDtoList(ANALYSIS_NAME);
    assertThat(reportDtoList.getReports().size()).isEqualTo(1);
    List<KibanaReportDetailsDto> reportDetailsDtoList = new ArrayList<>(reportDtoList.getReports());
    assertThat(reportDetailsDtoList.get(0).getId()).isEqualTo(REPORT_ID);
    assertThat(reportDetailsDtoList.get(0).getTitle()).isEqualTo(FILE_NAME);
  }

  @Test
  void shouldReturnTenantWrapperDto() {
    //given
    when(analysisService.getTenantIdByAnalysis(ANALYSIS_PREFIX + ANALYSIS_NAME))
        .thenReturn(ADMIN_TENANT);
    //when
    TenantNameWrapper tenantNameWrapperByAnalysis =
        underTest.getTenantNameWrapperByAnalysis(ANALYSIS_NAME);
    //then
    assertThat(tenantNameWrapperByAnalysis.getTenantName()).isEqualTo(ADMIN_TENANT);
  }

  @Test
  void shouldReturnReportDefinitionDto() {

    //given
    KibanaReportDefinitionDto mock = mock(KibanaReportDefinitionDto.class);

    when(opendistroKibanaClient.listReportDefinitions("prod_production_ai_reasoning"))
        .thenReturn(of(mock));

    when(mock.getId()).thenReturn(ANALYSIS_NAME);

    when(mock.getDescription()).thenReturn(REP_DEF_DESCRIPTION);

    //when
    ReportsDefinitionListDto kibanaReportDefinitionDtoList =
        underTest.getReportDefinitionsByTenant("ai_reasoning", "prod_production_ai_reasoning",
            REPORT_DEFINITION_NAME_PREFIX);

    //then
    assertThat(kibanaReportDefinitionDtoList.getReportDefinitionDtoList().size()).isEqualTo(1);

    ReportsDefinitionListDto.ReportDefinitionDto reportDefinitionDto =
        kibanaReportDefinitionDtoList.getReportDefinitionDtoList().get(0);

    assertThat(reportDefinitionDto.getReportType()).isEqualTo(REPORT_TYPE);
    assertThat(reportDefinitionDto.getTitle()).isEqualTo(REPORT_TITLE);
    assertThat(reportDefinitionDto.getDescription()).isEqualTo(REP_DEF_DESCRIPTION);
    assertThat(reportDefinitionDto.getName())
        .isEqualTo(REPORT_DEFINITION_NAME_PREFIX + ANALYSIS_NAME);
  }
}
