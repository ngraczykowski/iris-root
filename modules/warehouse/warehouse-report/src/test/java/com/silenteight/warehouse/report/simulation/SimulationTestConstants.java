package com.silenteight.warehouse.report.simulation;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_DEFINITION_ID;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_NAME;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SimulationTestConstants {

  static final ReportDefinitionDto REPORT_DEFINITION_DTO = ReportDefinitionDto.builder()
      .id(REPORT_DEFINITION_ID)
      .name("/analysis/a9b45451-6fde-4832-8dc0-d17b4708d8ca/reports/" + REPORT_DEFINITION_ID)
      .title(REPORT_NAME)
      .description("description")
      .build();
}
