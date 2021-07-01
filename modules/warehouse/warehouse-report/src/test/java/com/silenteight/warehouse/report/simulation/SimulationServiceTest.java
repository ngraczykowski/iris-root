package com.silenteight.warehouse.report.simulation;

import com.silenteight.warehouse.report.reporting.ReportStatus;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static java.lang.String.valueOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

  private static final String ANALYSIS_ID = "51ba8e70-1aef-48ce-8f12-3cf3cb053495";
  private static final Long TIMESTAMP = 1624542679L;
  private static final String DEFINITION_ID = "75149e08-3c4e-4726-88b6-775e6c2bc565";
  private static final String REPORT_INSTANCE_ID = "7ee3f480-4506-4b2f-a853-7e9a4a3e5162";
  private static final String EXPECTED_REPORT_NAME =
      "analysis/" + ANALYSIS_ID + "/definitions/" + DEFINITION_ID + "/reports/" + TIMESTAMP;

  @Mock
  ReportingService reportingService;

  @Mock
  SimulationReportingQuery simulationReportingQuery;

  @Mock
  UserAwareReportingService userAwareReportingService;

  @InjectMocks
  SimulationService underTest;

  @Test
  void shouldReturnGeneratingReportStatus() {
    //when
    when(simulationReportingQuery.getTenantIdByAnalysisId(ANALYSIS_ID))
        .thenReturn(ADMIN_TENANT);
    when(userAwareReportingService.getReportInstanceId(ADMIN_TENANT,
        DEFINITION_ID, TIMESTAMP))
        .thenReturn(empty());
    //given
    ReportStatus reportStatus =
        underTest.getReportGeneratingStatus(ANALYSIS_ID, DEFINITION_ID,
            TIMESTAMP);

    assertThat(valueOf(reportStatus.getStatus())).isEqualTo("GENERATING");
    assertThat(reportStatus.getReportName()).isEqualTo(EXPECTED_REPORT_NAME);
  }

  @Test
  void shouldReturnOkReportStatusOk() {
    //when
    when(simulationReportingQuery.getTenantIdByAnalysisId(ANALYSIS_ID)).thenReturn(ADMIN_TENANT);
    when(userAwareReportingService.getReportInstanceId(ADMIN_TENANT, DEFINITION_ID, TIMESTAMP))
        .thenReturn(of(REPORT_INSTANCE_ID));

    //given
    ReportStatus reportStatus =
        underTest.getReportGeneratingStatus(ANALYSIS_ID, DEFINITION_ID,
            TIMESTAMP);

    assertThat(valueOf(reportStatus.getStatus())).isEqualTo("OK");
    assertThat(reportStatus.getReportName()).isEqualTo(EXPECTED_REPORT_NAME);
  }
}