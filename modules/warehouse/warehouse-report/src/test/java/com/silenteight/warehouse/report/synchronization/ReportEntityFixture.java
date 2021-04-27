package com.silenteight.warehouse.report.synchronization;

import static com.silenteight.warehouse.report.synchronization.ReportFixture.FILENAME;
import static com.silenteight.warehouse.report.synchronization.ReportFixture.KIBANA_REPORT_INSTANCE_ID;
import static com.silenteight.warehouse.report.synchronization.ReportFixture.REPORT_ID;
import static com.silenteight.warehouse.report.synchronization.ReportFixture.TENANT;

public class ReportEntityFixture {

  public static final ReportEntity REPORT = ReportEntity.builder()
      .reportId(REPORT_ID)
      .kibanaReportInstanceId(KIBANA_REPORT_INSTANCE_ID)
      .filename(FILENAME)
      .tenant(TENANT)
      .build();
}
