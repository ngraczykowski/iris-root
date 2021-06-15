package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance.ReportDefinition;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance.ReportDefinitionDetails;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.KIBANA_REPORT_DTO;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_DEFINITION_ID;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAwareReportingServiceTest {

  private static final Long CREATED_BEFORE = 1L;
  private static final Long CREATED_NOW = 2L;
  private static final Long CREATED_AFTER = 3L;
  private static final Long CREATED_AFTER_AFTER = 4L;
  private static final String REPORT_INSTANCE_ID_BEFORE = "LCFXeXkB_K2MGH_UNBNE";
  private static final String REPORT_INSTANCE_ID_AFTER = "FlpJp3kBtnbt7Zpy1zHr";
  private static final String REPORT_INSTANCE_ID_AFTER_AFTER = "lVrjnHkBtnbt7ZpytDA6";
  private static final String OTHER_REPORT_DEFINITION_ID = "otherReportDefinitionId";
  private static final String TENANT = "tenant";

  @Mock
  private OpendistroElasticClient opendistroElasticClient;
  @Mock
  private OpendistroKibanaClient opendistroKibanaClient;
  @InjectMocks
  UserAwareReportingService underTest;

  @Test
  void shouldReturnFirstReportAfterTimestamp() {
    mockListReportsInstancesResponse(of(
        reportInstance(CREATED_BEFORE, REPORT_INSTANCE_ID_BEFORE, REPORT_DEFINITION_ID),
        reportInstance(CREATED_AFTER, REPORT_INSTANCE_ID_AFTER, REPORT_DEFINITION_ID),
        reportInstance(CREATED_AFTER_AFTER, REPORT_INSTANCE_ID_AFTER_AFTER, REPORT_DEFINITION_ID)));
    mockExpectedReportInstance(REPORT_INSTANCE_ID_AFTER);

    KibanaReportDto kibanaReportDto =
        underTest.downloadReport(TENANT, REPORT_DEFINITION_ID, CREATED_NOW);

    assertThat(kibanaReportDto).isNotNull();
  }

  @Test
  void shouldReturnMatchingReportDefinition() {
    mockListReportsInstancesResponse(of(
        reportInstance(CREATED_AFTER, REPORT_INSTANCE_ID_AFTER, OTHER_REPORT_DEFINITION_ID),
        reportInstance(CREATED_AFTER_AFTER, REPORT_INSTANCE_ID_AFTER_AFTER, REPORT_DEFINITION_ID)));
    mockExpectedReportInstance(REPORT_INSTANCE_ID_AFTER_AFTER);

    KibanaReportDto kibanaReportDto =
        underTest.downloadReport(TENANT, REPORT_DEFINITION_ID, CREATED_NOW);

    assertThat(kibanaReportDto).isNotNull();
  }

  @Test
  void shouldThrowExceptionIfReportNotFound() {
    mockListReportsInstancesResponse(of());
    assertThatThrownBy(
        () -> underTest.downloadReport(TENANT, REPORT_DEFINITION_ID, CREATED_NOW))
        .isInstanceOf(ReportInstanceNotFoundException.class);
  }

  void mockListReportsInstancesResponse(List<ReportInstance> instances) {
    ListReportsInstancesResponse listReportsInstancesResponse =
        ListReportsInstancesResponse.builder()
            .reportInstancesList(instances)
            .build();

    when(opendistroElasticClient.getReportInstances(any()))
        .thenReturn(listReportsInstancesResponse);
  }

  void mockExpectedReportInstance(String reportInstanceId) {
    when(opendistroKibanaClient.getReportContent(TENANT, reportInstanceId))
        .thenReturn(KIBANA_REPORT_DTO);
  }

  static ReportInstance reportInstance(
      Long createdTimeMs, String reportInstanceId, String reportDefinitionId) {

    return ReportInstance.builder()
        .id(reportInstanceId)
        .status("Created")
        .createdTimeMs(createdTimeMs)
        .reportDefinitionDetails(
            reportDefinition(reportDefinitionId))
        .build();
  }

  static ReportDefinitionDetails reportDefinition(String reportDefinitionId) {
    return ReportDefinitionDetails.builder()
        .id(reportDefinitionId)
        .reportDefinition(ReportDefinition.builder()
            .name("name")
            .build())
        .build();
  }
}
