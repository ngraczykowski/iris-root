package com.silenteight.warehouse.report.synchronization;

import java.util.UUID;

import static java.util.UUID.fromString;

public class ReportFixture {

  private ReportFixture() {
  }

  public static final UUID REPORT_ID = fromString("e7a253cd-2a99-432b-93e1-c5e24ca0b076");
  public static final String KIBANA_REPORT_INSTANCE_ID = "ewsf_XgBis_5rx12hajb";
  public static final String FILENAME =
      "all-alerts-report_2021-04-23T05:07:17.618Z_c6697d20-a3f1-11eb-81b7-279379fd819f.csv";
  public static final String TENANT = "admin_tenant";
}
