package com.silenteight.sens.webapp.reportscb;

import com.silenteight.sens.webapp.backend.report.api.Report;
import com.silenteight.sens.webapp.backend.report.api.ReportGenerator;
import com.silenteight.sens.webapp.backend.report.api.ReportLinesReader;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

class SecurityMatrixReportGenerator implements ReportGenerator {

  private static final String REPORT_NAME = "security-matrix-report";
  private static final String FILE_NAME = REPORT_NAME + ".csv";
  private static final Charset ENCODING = UTF_8;

  private final ReportLinesReader reportLinesReader = new ReportLinesReader(FILE_NAME, ENCODING);

  @Override
  public String getName() {
    return REPORT_NAME;
  }

  @Override
  public Report generateReport(Map<String, String> parameters) {
    return new SimpleReport(FILE_NAME, getReportData());
  }

  private List<String> getReportData() {
    return reportLinesReader
        .readLines()
        .collect(toList());
  }
}
