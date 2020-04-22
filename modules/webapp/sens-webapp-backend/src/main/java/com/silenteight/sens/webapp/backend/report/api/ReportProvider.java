package com.silenteight.sens.webapp.backend.report.api;

public interface ReportProvider {

  ReportGenerator getReportGenerator(String reportName);
}
