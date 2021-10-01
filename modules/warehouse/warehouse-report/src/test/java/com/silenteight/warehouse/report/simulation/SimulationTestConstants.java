package com.silenteight.warehouse.report.simulation;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class SimulationTestConstants {

  static final String REPORT_DEFINITION_ID = "248cb531-1ca3-4496-a7e4-1551ee6c90f4";
  static final String REPORT_NAME = "Test Report";

  static final ReportDefinitionDto REPORT_DEFINITION_DTO = ReportDefinitionDto.builder()
      .id(REPORT_DEFINITION_ID)
      .name("/analysis/a9b45451-6fde-4832-8dc0-d17b4708d8ca/reports/" + REPORT_DEFINITION_ID)
      .title(REPORT_NAME)
      .description("description")
      .build();
}
