package com.silenteight.sens.webapp.backend.report.api;

import java.util.Map;

public interface ReportGenerator {

  String getName();

  Report generateReport(Map<String, String> parameters);
}
