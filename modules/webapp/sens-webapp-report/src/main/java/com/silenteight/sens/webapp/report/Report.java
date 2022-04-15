package com.silenteight.sens.webapp.report;

import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;

public interface Report {

  String getReportFileName();

  LinesSupplier getReportContent();
}
