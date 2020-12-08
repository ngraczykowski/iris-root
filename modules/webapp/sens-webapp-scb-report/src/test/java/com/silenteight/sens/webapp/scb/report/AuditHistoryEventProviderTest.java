package com.silenteight.sens.webapp.scb.report;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditDataDto.AuditDataDtoBuilder;
import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.report.ReportGenerationDetails;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditHistoryEventProviderTest {

  private static final String REPORT_GENERATED_EVENT_TYPE = "ReportGenerated";
  private static final String AUDIT_REPORT_NAME = "audit-report";

  private static final JsonConversionHelper JSON_CONVERTER = JsonConversionHelper.INSTANCE;

  @Mock
  private AuditingFinder auditingFinder;

  @InjectMocks
  private AuditHistoryEventProvider underTest;

  @Test
  void returnsAuditHistoryEventsFromAuditData() {
    String principal1 = "jdoe";
    String principal2 = "asmith";
    Timestamp timestamp1 = Timestamp.valueOf("2020-04-15 12:14:32");
    Timestamp timestamp2 = Timestamp.valueOf("2020-06-10 09:26:51");
    String ipAddress1 = "192.122.0.8";
    String ipAddress2 = "192.154.0.1";
    String reportGenerationStatus = "SUCCESS";

    OffsetDateTime from = now().minusHours(6);
    OffsetDateTime to = now();

    when(auditingFinder.find(from, to, singletonList(REPORT_GENERATED_EVENT_TYPE)))
        .thenReturn(
            List.of(
                auditDataDtoWithDefaults()
                    .entityId(AUDIT_REPORT_NAME)
                    .timestamp(timestamp1)
                    .type(REPORT_GENERATED_EVENT_TYPE)
                    .principal(principal1)
                    .details(
                        jsonStringOf(
                            ReportGenerationDetails.builder()
                                .status(reportGenerationStatus)
                                .ipAddress(ipAddress1)
                                .build()))
                    .build(),
                auditDataDtoWithDefaults()
                    .entityId(AUDIT_REPORT_NAME)
                    .timestamp(timestamp2)
                    .type(REPORT_GENERATED_EVENT_TYPE)
                    .principal(principal2)
                    .details(
                        jsonStringOf(
                            ReportGenerationDetails.builder()
                                .status(reportGenerationStatus)
                                .ipAddress(ipAddress2)
                                .build()))
                    .build()));

    List<AuditHistoryEventDto> events = underTest.provide(from, to);

    assertThat(events).hasSize(2);
    assertThat(events).isEqualTo(
        List.of(
            AuditHistoryEventDto.builder()
                .username(principal1)
                .status(reportGenerationStatus)
                .ipAddress(ipAddress1)
                .timestamp(timestamp1.toInstant())
                .build(),
            AuditHistoryEventDto.builder()
                .username(principal2)
                .status(reportGenerationStatus)
                .ipAddress(ipAddress2)
                .timestamp(timestamp2.toInstant())
                .build()));
  }

  private AuditDataDtoBuilder auditDataDtoWithDefaults() {
    return AuditDataDto.builder()
        .eventId(randomUUID())
        .correlationId(randomUUID())
        .timestamp(Timestamp.valueOf(LocalDateTime.now()));
  }

  private static String jsonStringOf(Object details) {
    JSON_CONVERTER.objectMapper().disable(WRITE_DATES_AS_TIMESTAMPS);
    try {
      return JSON_CONVERTER.serializeToString(details);
    } finally {
      JSON_CONVERTER.objectMapper().enable(WRITE_DATES_AS_TIMESTAMPS);
    }
  }
}
