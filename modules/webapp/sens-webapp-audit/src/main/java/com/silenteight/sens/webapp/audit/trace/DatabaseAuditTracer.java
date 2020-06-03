package com.silenteight.sens.webapp.audit.trace;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import java.sql.Timestamp;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Slf4j
@RequiredArgsConstructor
class DatabaseAuditTracer implements AuditTracer {

  private static final String EMPTY_DETAILS = "{}";
  private static final JsonConversionHelper JSON_CONVERTER = JsonConversionHelper.INSTANCE;

  @NonNull
  private final AuditingLogger auditingLogger;

  @Override
  public void save(AuditEvent auditEvent) {
    auditingLogger.log(createAuditData(auditEvent));
  }

  private static AuditDataDto createAuditData(AuditEvent auditEvent) {
    return AuditDataDto
        .builder()
        .eventId(auditEvent.getId())
        .correlationId(auditEvent.getCorrelationId())
        .timestamp(Timestamp.from(auditEvent.getTimestamp()))
        .type(auditEvent.getType())
        .principal(auditEvent.getPrincipal())
        .entityId(auditEvent.getEntityId())
        .entityClass(auditEvent.getEntityClass())
        .entityAction(auditEvent.getEntityAction())
        .details(createDetailsAsJsonString(auditEvent.getDetails()))
        .build();
  }

  private static String createDetailsAsJsonString(Object details) {
    if (details == null) {
      return EMPTY_DETAILS;
    } else {
      try {
        JSON_CONVERTER.objectMapper()
            .disable(WRITE_DATES_AS_TIMESTAMPS);
        return JSON_CONVERTER.serializeToString(details);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return EMPTY_DETAILS;
      } finally {
        JSON_CONVERTER.objectMapper()
            .enable(WRITE_DATES_AS_TIMESTAMPS);
      }
    }
  }
}
