package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.report.reporting.ReportStatus;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.production.ProductionReportType.ACCURACY;
import static java.lang.String.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductionServiceTest {

  private static final String ADMIN_TENANT = "admin_tenant";
  private static final String REPORT_INSTANCE_ID = "7ee3f480-4506-4b2f-a853-7e9a4a3e5162";
  private static final String DEFINITION_ID = "32e48230-17fc-4bd2-aaa6-d9548f10ceb8";
  private static final Long TIMESTAMP = 1622009305142L;
  private static final String EXPECTED_REPORT_DOWNLOAD_URL =
      "/v1/analysis/production/definitions/" + ACCURACY + "/" + DEFINITION_ID + "/reports/"
          + TIMESTAMP;

  @Mock
  ReportingService reportingService;

  @Mock
  UserAwareReportingService userAwareReportingService;

  @Mock
  ProductionReportingQuery productionReportingQuery;

  @InjectMocks
  ProductionService underTest;

  @Test
  void shouldReturnGeneratingReportStatus() {
    //when
    when(productionReportingQuery.getTenantName(ACCURACY)).thenReturn(ADMIN_TENANT);
    when(userAwareReportingService.getReportInstanceId(ADMIN_TENANT,
            DEFINITION_ID, TIMESTAMP))
        .thenReturn(empty());
    //given
    ReportStatus reportStatus =
        underTest.getReportGeneratingStatus(ACCURACY, DEFINITION_ID,
            TIMESTAMP);

    assertThat(valueOf(reportStatus.getStatus())).isEqualTo("GENERATING");
  }

  @Test
  void shouldReturnOkReportStatusOk() {
    //when
    when(productionReportingQuery.getTenantName(ACCURACY)).thenReturn(ADMIN_TENANT);
    when(userAwareReportingService.getReportInstanceId(
            ADMIN_TENANT,
            DEFINITION_ID, TIMESTAMP))
        .thenReturn(of(REPORT_INSTANCE_ID));

    //given
    ReportStatus reportStatus =
        underTest.getReportGeneratingStatus(ACCURACY, DEFINITION_ID,
            TIMESTAMP);

    assertThat(valueOf(reportStatus.getStatus())).isEqualTo("OK");
    assertThat(reportStatus.getDownloadReportUrl()).isEqualTo(EXPECTED_REPORT_DOWNLOAD_URL);
  }
}