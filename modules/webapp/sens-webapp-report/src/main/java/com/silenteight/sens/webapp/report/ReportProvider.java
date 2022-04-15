package com.silenteight.sens.webapp.report;

public interface ReportProvider {

  ReportGenerator getReportGenerator(String reportName);
}
