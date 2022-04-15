package com.silenteight.sens.webapp.scb.report;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.report.Report;
import com.silenteight.sens.webapp.report.ReportGenerator;
import com.silenteight.sens.webapp.report.ReportLinesReader;
import com.silenteight.sens.webapp.report.SimpleReport;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

@Slf4j
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
    log.debug("Generating Security Matrix Report with parameters={}", parameters);

    return new SimpleReport(FILE_NAME, getReportData());
  }

  private List<String> getReportData() {
    return reportLinesReader
        .readLines()
        .collect(toList());
  }
}
