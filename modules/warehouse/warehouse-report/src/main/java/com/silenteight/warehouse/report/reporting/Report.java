package com.silenteight.warehouse.report.reporting;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface Report {

  String getReportName();

  InputStream getInputStream() throws FileNotFoundException;
}
