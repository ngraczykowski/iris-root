package com.silenteight.warehouse.common.opendistro.kibana;

public class KibanaReportFixture {

  public static final String REPORT_DEFINITION_ID = "lVrjnHkBtnbt7ZpytDA6";
  public static final String REPORT_NAME = "ai-reasoning-30-days";
  public static final String REPORT_DESCRIPTION = "This is AI reasoning report";

  public static final KibanaReportDefinitionDto KIBANA_REPORT_DEFINITION_DTO =
      KibanaReportDefinitionDto.builder()
          .id(REPORT_DEFINITION_ID)
          .reportName(REPORT_NAME)
          .description(REPORT_DESCRIPTION)
          .build();

  public static final String REPORT_FILENAME =
      "all-alerts-report_2021-04-26T10_11_06.611Z_b6fa5430-a677-11eb-887b-8512c00572fb.csv";
  public static final String REPORT_CONTENT = "_id/n123";

  public static final KibanaReportDto KIBANA_REPORT_DTO = KibanaReportDto.builder()
      .filename(REPORT_FILENAME)
      .content(REPORT_CONTENT)
      .build();
}
