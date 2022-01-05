package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.report.reporting.*;
import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductionReportsServiceTest {

  ReportProperties reportProperties = mock(ReportProperties.class);

  AiReasoningReportProperties aiReasoningReportProperties = mock(AiReasoningReportProperties.class);
  AiReasoningMatchLevelReportProperties matchLevelReportProperties =
      mock(AiReasoningMatchLevelReportProperties.class);
  MetricsReportProperties metricsReportProperties = mock(MetricsReportProperties.class);
  AiReasoningReportDefinitionProperties aiReasoningReportDefinitionProperties =
      mock(AiReasoningReportDefinitionProperties.class);
  PropertiesDefinition propertiesDefinition = mock(PropertiesDefinition.class);

  ProductionReportsService underTest = new ProductionReportsService(reportProperties);

  ReportTypeDto reportTypeDto1 = ReportTypeDto.builder()
      .type("AI_REASONING")
      .title("AI Reasoning")
      .name("analysis/production/reports/AI_REASONING")
      .build();

  ReportTypeDto reportTypeDto2 = ReportTypeDto.builder()
      .type("METRICS")
      .title("Metrics")
      .name("analysis/production/reports/METRICS")
      .build();

  @Before
  public void setup() {
    underTest = new ProductionReportsService(reportProperties);
  }

  @Test
  public void shouldReturnEmptyListWhenPropertiesAreEmpty() {
    assertThat(underTest.getListOfReports()).isEqualTo(Lists.emptyList());
  }

  @Test
  public void shouldReturnListWithConfiguredProperties() {
    when(reportProperties.getAiReasoning()).thenReturn(aiReasoningReportProperties);
    when(aiReasoningReportProperties.getProduction())
        .thenReturn(aiReasoningReportDefinitionProperties);
    when(reportProperties.getAiReasoningMatchLevel()).thenReturn(matchLevelReportProperties);
    when(reportProperties.getMetrics()).thenReturn(metricsReportProperties);
    when(metricsReportProperties.getProduction()).thenReturn(propertiesDefinition);

    assertThat(underTest.getListOfReports()).hasSize(2);
    assertThat(underTest.getListOfReports()).contains(reportTypeDto1, reportTypeDto2);
  }
}