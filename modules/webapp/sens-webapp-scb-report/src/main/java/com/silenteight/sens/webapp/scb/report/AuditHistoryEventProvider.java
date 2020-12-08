package com.silenteight.sens.webapp.scb.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.report.ReportGenerationDetails;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AuditHistoryEventProvider {

  private static final JsonConversionHelper JSON_CONVERTER = JsonConversionHelper.INSTANCE;

  private static final String REPORT_GENERATED_EVENT_TYPE = "ReportGenerated";
  private static final String AUDIT_REPORT_NAME = "audit-report";

  @NonNull
  private final AuditingFinder auditingFinder;

  List<AuditHistoryEventDto> provide(@NonNull OffsetDateTime from, @NonNull OffsetDateTime to) {
    return auditingFinder
        .find(from, to, singletonList(REPORT_GENERATED_EVENT_TYPE))
        .stream()
        .filter(AuditHistoryEventProvider::isAuditReport)
        .map(AuditHistoryEventProvider::mapToDto)
        .collect(toList());
  }

  private static boolean isAuditReport(AuditDataDto auditData) {
    return StringUtils.equals(auditData.getEntityId(), AUDIT_REPORT_NAME);
  }

  private static AuditHistoryEventDto mapToDto(AuditDataDto auditData) {
    ReportGenerationDetails details = deserializeDetails(auditData.getDetails());

    return AuditHistoryEventDto.builder()
        .username(auditData.getPrincipal())
        .status(details.getStatus())
        .ipAddress(details.getIpAddress())
        .timestamp(auditData.getTimestamp().toInstant())
        .build();
  }

  private static ReportGenerationDetails deserializeDetails(String details) {
    return JSON_CONVERTER.deserializeObject(
        JSON_CONVERTER.deserializeFromString(details), ReportGenerationDetails.class);
  }
}
