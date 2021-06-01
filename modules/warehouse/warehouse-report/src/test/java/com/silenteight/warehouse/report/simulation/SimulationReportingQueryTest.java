package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.report.reporting.ReportingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS_ID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationReportingQueryTest {

  @Mock
  private ReportingService reportingService;

  @InjectMocks
  private SimulationReportingQuery underTest;

  @Test
  void shouldReturnTenantWrapperDto() {
    //given
    when(reportingService.getTenantIdByAnalysisId(ANALYSIS_ID))
        .thenReturn(ADMIN_TENANT);
    //when
    TenantDto tenantNameWrapperByAnalysis =
        underTest.getTenantDtoByAnalysisId(ANALYSIS_ID);
    //then
    assertThat(tenantNameWrapperByAnalysis.getTenantName()).isEqualTo(ADMIN_TENANT);
  }
}
