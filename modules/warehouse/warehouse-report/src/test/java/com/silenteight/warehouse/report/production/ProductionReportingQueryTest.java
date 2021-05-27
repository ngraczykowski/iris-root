package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.report.reporting.ReportingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.report.production.ProductionControllerTestConstants.AI_REASONING_TYPE;
import static com.silenteight.warehouse.report.production.ProductionControllerTestConstants.REPORTS_DEFINITION_DTOS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
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
  void shouldReturnCorrectTenantName() {
    when(environmentProperties.getPrefix()).thenReturn("test");

    ArgumentCaptor<String> argumentCaptor = forClass(String.class);

    when(reportingService.getReportDefinitionsByTenant(anyString(), argumentCaptor.capture(),
        anyString())).thenReturn(REPORTS_DEFINITION_DTOS);

    underTest.getReportsDefinitions(AI_REASONING_TYPE);

    assertThat(argumentCaptor.getValue()).isEqualTo("test_production_ai_reasoning");
  }
}
