package com.silenteight.sens.webapp.backend.report;

public interface ReportProvider {

  ReportGenerator getReportGenerator(String reportName);
}
