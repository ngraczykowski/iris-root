package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;
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
import static com.silenteight.warehouse.report.production.ProductionControllerTestConstants.AI_REASONING_TYPE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductionReportingQueryTest {

  @Mock
  private ReportingService reportingService;

  @Mock
  private EnvironmentProperties environmentProperties;

  @InjectMocks
  private ProductionReportingQuery underTest;

  @Test
  void shouldReturnReportDefinitions() {
    when(environmentProperties.getPrefix()).thenReturn("env");
    when(reportingService.listReportDefinitions("env_production_ai_reasoning"))
        .thenReturn(List.of(KIBANA_REPORT_DEFINITION_DTO));

    List<ReportDefinitionDto> reportsDefinitions =
        underTest.getReportsDefinitions(AI_REASONING_TYPE);

    assertThat(reportsDefinitions).hasSize(1);
    ReportDefinitionDto dto = reportsDefinitions.get(0);

    assertThat(dto.getId()).isEqualTo(REPORT_DEFINITION_ID);
    assertThat(dto.getName()).isEqualTo(
        "/v1/analysis/production/definitions/" + REPORT_DEFINITION_ID);
    assertThat(dto.getTitle()).isEqualTo(AI_REASONING_TYPE.getTitle());
    assertThat(dto.getDescription()).isEqualTo(REPORT_DESCRIPTION);
  }
}
