package com.silenteight.sens.webapp.backend.audit.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.report.Report;
import com.silenteight.sens.webapp.backend.report.ReportGenerator;
import com.silenteight.sens.webapp.backend.report.SimpleReport;
import com.silenteight.sens.webapp.common.time.DateFormatter;
import com.silenteight.sens.webapp.common.time.TimeSource;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@Slf4j
@RequiredArgsConstructor
class AuditReportGenerator implements ReportGenerator {

  private static final String REPORT_NAME = "audit-report";
  private static final String HEADER =
      "event_id,parent_event_id,correlation_id,timestamp,type,principal,entity_id,entity_class,"
          + "entity_action,details";

  @NonNull
  private final TimeSource timeProvider;
  @NonNull
  private final DateFormatter dateFormatter;

  @Override
  public String getName() {
    return REPORT_NAME;
  }

  @Override
  public Report generateReport(Map<String, String> parameters) {
    return new SimpleReport(getReportFileName(), getReportData());
  }

  private String getReportFileName() {
    return "audit-" + getFormattedCurrentDate() + ".csv";
  }

  private String getFormattedCurrentDate() {
    return dateFormatter.format(timeProvider.now());
  }

  private static List<String> getReportData() {
    // TODO(mmastylo) fill report with data
    return of(HEADER).collect(toList());
  }
}
