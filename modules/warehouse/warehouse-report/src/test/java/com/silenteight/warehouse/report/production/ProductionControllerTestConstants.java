package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

public class ProductionControllerTestConstants {

  static final ReportType AI_REASONING_TYPE = ReportType.AI_REASONING;
  static final String GET_REPORT_DEF_BY_TYPE_AI_REASONING =
      "/v1/analysis/production/definitions?type=" + AI_REASONING_TYPE;

  static final ReportDefinitionDto REPORT_DEFINITION_DTO =
      ReportDefinitionDto.builder()
          .id("a9b45451-6fde-4832-8dc0-d17b4708d8ca")
          .name("/analysis/production/reports/a9b45451-6fde-4832-8dc0-d17b4708d8ca")
          .title("AI REASONING")
          .description("Last month")
          .reportType("AI_REASONING")
          .build();
}
