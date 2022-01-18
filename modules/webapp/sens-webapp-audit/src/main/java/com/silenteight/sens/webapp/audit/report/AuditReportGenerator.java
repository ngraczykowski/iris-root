package com.silenteight.sens.webapp.audit.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.dto.AuditLogDto;
import com.silenteight.sens.webapp.audit.api.list.ListAuditLogsQuery;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.report.Report;
import com.silenteight.sens.webapp.report.ReportGenerator;
import com.silenteight.sens.webapp.report.SimpleReport;
import com.silenteight.sens.webapp.report.exception.IllegalParameterException;
import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.base.common.time.TimeSource;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.silenteight.sep.base.common.time.ApplicationTimeZone.TIME_ZONE;
import static java.time.OffsetDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

@Slf4j
@RequiredArgsConstructor
class AuditReportGenerator implements ReportGenerator {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(TIME_ZONE.toZoneId());

  private static final String REPORT_NAME = "audit-report";
  private static final String FROM_PARAMETER_NAME = "from";
  private static final String TO_PARAMETER_NAME = "to";

  private static final String EVENT_ID_COLUMN_HEADER = "event_id";
  private static final String CORRELATION_ID_COLUMN_HEADER = "correlation_id";
  private static final String TIMESTAMP_COLUMN_HEADER = "timestamp";
  private static final String TYPE_COLUMN_HEADER = "type";
  private static final String PRINCIPAL_COLUMN_HEADER = "principal";
  private static final String ENTITY_ID_COLUMN_HEADER = "entity_id";
  private static final String ENTITY_CLASS_COLUMN_HEADER = "entity_class";
  private static final String ENTITY_ACTION_COLUMN_HEADER = "entity_action";
  private static final String DETAILS_COLUMN_HEADER = "details";

  @NonNull
  private final TimeSource timeProvider;
  @NonNull
  private final DateFormatter dateFormatter;
  @NonNull
  private final ListAuditLogsQuery listAuditLogsQuery;

  @Override
  public String getName() {
    return REPORT_NAME;
  }

  @Override
  public Report generateReport(Map<String, String> parameters) {
    OffsetDateTime from = getFrom(parameters);
    OffsetDateTime to = getTo(parameters);
    return new SimpleReport(getReportFileName(), getReportData(from, to));
  }

  private String getReportFileName() {
    return "audit-" + getFormattedCurrentDate() + ".csv";
  }

  private String getFormattedCurrentDate() {
    return dateFormatter.format(timeProvider.now());
  }

  private static OffsetDateTime getFrom(Map<String, String> parameters) {
    return getBound(parameters, FROM_PARAMETER_NAME);
  }

  private static OffsetDateTime getTo(Map<String, String> parameters) {
    return getBound(parameters, TO_PARAMETER_NAME);
  }

  private static OffsetDateTime getBound(Map<String, String> parameters, String parameterName) {
    String bound = parameters.get(parameterName);
    if (bound == null)
      throw new IllegalParameterException(parameterName + " not provided");

    try {
      return parse(bound);
    } catch (Exception e) {
      throw new IllegalParameterException(parameterName + " must be datetime", e);
    }
  }

  private List<String> getReportData(OffsetDateTime from, OffsetDateTime to) {
    Collection<AuditLogDto> auditLogs = listAuditLogsQuery.list(from, to);
    return buildReportData(auditLogs).collect(toList());
  }

  private static Stream<String> buildReportData(Collection<AuditLogDto> auditLogs) {
    return of(new CsvBuilder<>(auditLogs.stream())
        .cell(EVENT_ID_COLUMN_HEADER, a -> a.getEventId().toString())
        .cell(CORRELATION_ID_COLUMN_HEADER, a -> a.getCorrelationId().toString())
        .cell(TIMESTAMP_COLUMN_HEADER, a -> formatDate(a.getTimestamp()))
        .cell(TYPE_COLUMN_HEADER, AuditLogDto::getType)
        .cell(PRINCIPAL_COLUMN_HEADER, AuditLogDto::getPrincipal)
        .cell(ENTITY_ID_COLUMN_HEADER, AuditLogDto::getEntityId)
        .cell(ENTITY_CLASS_COLUMN_HEADER, AuditLogDto::getEntityClass)
        .cell(ENTITY_ACTION_COLUMN_HEADER, AuditLogDto::getEntityAction)
        .cell(DETAILS_COLUMN_HEADER, AuditLogDto::getDetails)
        .build());
  }

  private static String formatDate(OffsetDateTime timestamp) {
    return TIMESTAMP_FORMATTER.format(timestamp);
  }
}
