package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

public class ProductionControllerTestConstants {

  static final ProductionReportType AI_REASONING_TYPE = ProductionReportType.AI_REASONING;

  static final ReportDefinitionDto REPORT_DEFINITION_DTO =
      ReportDefinitionDto.builder()
          .id("a9b45451-6fde-4832-8dc0-d17b4708d8ca")
          .name("/analysis/production/reports/a9b45451-6fde-4832-8dc0-d17b4708d8ca")
          .title("AI REASONING")
          .description("Last month")
          .reportType("AI_REASONING")
          .build();
}
