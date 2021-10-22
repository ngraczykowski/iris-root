package com.silenteight.warehouse.report.reasoning.v1.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.warehouse.report.reasoning.v1.DeprecatedAiReasoningReportTestFixtures.ANALYSIS_ID;
import static com.silenteight.warehouse.report.reasoning.v1.DeprecatedAiReasoningReportTestFixtures.SIMULATION_DEFINITION_ID;
import static com.silenteight.warehouse.report.reasoning.v1.DeprecatedAiReasoningReportTestFixtures.SIMULATION_REPORT_DESC;
import static com.silenteight.warehouse.report.reasoning.v1.DeprecatedAiReasoningReportTestFixtures.SIMULATION_REPORT_TITLE;
import static org.assertj.core.api.Assertions.*;

class DeprecatedAiReasoningReportProviderTest {

  private final DeprecatedAiReasoningReportProvider
      underTest = new DeprecatedAiReasoningReportProvider();

  @Test
  void shouldGetSimulationReportDefinition() {
    // when
    List<ReportDefinitionDto> reportDefinitions = underTest.getReportDefinitions(ANALYSIS_ID);

    // then
    assertThat(reportDefinitions).hasSize(1);
    ReportDefinitionDto reportDefinitionDto = reportDefinitions.get(0);
    assertThat(reportDefinitionDto.getTitle()).isEqualTo(SIMULATION_REPORT_TITLE);
    assertThat(reportDefinitionDto.getName()).isEqualTo(getReportName());
    assertThat(reportDefinitionDto.getDescription()).isEqualTo(SIMULATION_REPORT_DESC);
  }

  private String getReportName() {
    return "analysis/" + ANALYSIS_ID + "/definitions/AI_REASONING/" +
        SIMULATION_DEFINITION_ID + "/reports";
  }
}