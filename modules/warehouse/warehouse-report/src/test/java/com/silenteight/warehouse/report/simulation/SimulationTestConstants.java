package com.silenteight.warehouse.report.simulation;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class SimulationTestConstants {

  static final String REPORT_TYPE = "Test Report Type";
  static final String REPORT_TITLE = "Test Report Title";
  static final String ANALYSIS_ID = "a9b45451-6fde-4832-8dc0-d17b4708d8ca";
  static final String REPORT_NAME = "/analysis/" + ANALYSIS_ID + "/reports/101";

  static final ReportTypeDto REPORT_DEFINITION_DTO = ReportTypeDto.builder()
      .name(REPORT_NAME)
      .type(REPORT_TYPE)
      .title(REPORT_TITLE)
      .build();
}
