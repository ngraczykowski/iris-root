package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
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
import static com.silenteight.warehouse.report.reporting.ReportServiceTestConstants.ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reporting.ReportServiceTestConstants.FILE_NAME;
import static com.silenteight.warehouse.report.reporting.ReportServiceTestConstants.LIST_REPORTS_INSTANCES_RESPONSE;
import static com.silenteight.warehouse.report.reporting.ReportServiceTestConstants.REPORT_ID;
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
}
