package com.silenteight.warehouse.report.accuracy.list;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.ANALYSIS_ID;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.SIMULATION_DEFINITION_ID;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.SIMULATION_REPORT_DESC;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.SIMULATION_REPORT_TITLE;
import static org.assertj.core.api.Assertions.*;

class AccuracyReportProviderTest {

  private final AccuracyReportProvider underTest = new AccuracyReportProvider();

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
    return "analysis/" + ANALYSIS_ID + "/definitions/ACCURACY/" +
        SIMULATION_DEFINITION_ID + "/reports";
  }
}