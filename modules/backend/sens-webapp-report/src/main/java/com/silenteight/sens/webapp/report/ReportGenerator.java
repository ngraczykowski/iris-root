package com.silenteight.sens.webapp.report;

import java.util.Map;

public interface ReportGenerator {

  String getName();

  Report generateReport(Map<String, String> parameters);
}
