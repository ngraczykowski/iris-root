package com.silenteight.warehouse.report.reporting;

import java.nio.file.Path;

public interface Report {

  String getReportName();

  Path getReportPath();

  Long getReportId();
}
