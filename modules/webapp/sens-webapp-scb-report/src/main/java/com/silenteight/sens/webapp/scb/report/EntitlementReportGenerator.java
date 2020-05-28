package com.silenteight.sens.webapp.scb.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.report.Report;
import com.silenteight.sens.webapp.report.ReportGenerator;
import com.silenteight.sens.webapp.report.ReportLinesReader;
import com.silenteight.sens.webapp.report.SimpleReport;
import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.base.common.time.TimeSource;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class EntitlementReportGenerator implements ReportGenerator {

  private static final String REPORT_NAME = "entitlement-report";
  private static final String FILE_NAME = REPORT_NAME + ".csv";
  private static final Charset ENCODING = UTF_8;

  @NonNull
  private final TimeSource timeProvider;
  @NonNull
  private final DateFormatter dateFormatter;
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
    //TODO(kdzieciol): Update this report after PO's decision, which roles are valid
    return reportLinesReader
        .readLines()
        .map(l -> format(l, getFormattedDate()))
        .collect(toList());
  }

  private String getFormattedDate() {
    return dateFormatter.format(timeProvider.now());
  }
}
