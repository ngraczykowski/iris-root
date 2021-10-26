package com.silenteight.warehouse.report.reasoning.match.list;

import com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AiReasoningMatchLevelReportProviderTest {

  private final AiReasoningMatchLevelReportProvider
      underTest = new AiReasoningMatchLevelReportProvider();

  @Test
  void shouldGetSimulationReportDefinition() {
    // when
    List<ReportDefinitionDto> reportDefinitions = underTest.getReportDefinitions(
        AiReasoningMatchLevelReportTestFixtures.ANALYSIS_ID);

    // then
    assertThat(reportDefinitions).hasSize(1);
    ReportDefinitionDto reportDefinitionDto = reportDefinitions.get(0);
    assertThat(reportDefinitionDto.getTitle()).isEqualTo(
        AiReasoningMatchLevelReportTestFixtures.SIMULATION_REPORT_TITLE);
    assertThat(reportDefinitionDto.getName()).isEqualTo(getReportName());
    assertThat(reportDefinitionDto.getDescription()).isEqualTo(
        AiReasoningMatchLevelReportTestFixtures.SIMULATION_REPORT_DESC);
  }

  private String getReportName() {
    return "analysis/" + AiReasoningMatchLevelReportTestFixtures.ANALYSIS_ID
        + "/definitions/AI_REASONING_MATCH_LEVEL/" +
        AiReasoningMatchLevelReportTestFixtures.SIMULATION_DEFINITION_ID + "/reports";
  }
}