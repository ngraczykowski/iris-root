package com.silenteight.sens.webapp.audit.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.audit.bs.api.v1.AuditData;

import org.springframework.dao.DataIntegrityViolationException;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;
import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;

@Slf4j
@RequiredArgsConstructor
public class AuditLogService {

  @NonNull
  private final AuditLogRepository repository;

  public void log(AuditData auditData) {
    AuditLog auditLog = toAuditLog(auditData);

    try {
      repository.save(auditLog);
      log.debug(INTERNAL, "Saved auditLog={}", auditLog);
    } catch (DataIntegrityViolationException e) {
      log.warn(INTERNAL, "Audit log for eventId={} already exists", auditLog.getEventId());
    }
  }

  private static AuditLog toAuditLog(AuditData auditData) {
    return AuditLog.builder()
        .eventId(toJavaUuid(auditData.getEventId()))
        .correlationId(toJavaUuid(auditData.getCorrelationId()))
        .timestamp(toOffsetDateTime(auditData.getTimestamp()))
        .type(auditData.getType())
        .principal(auditData.getPrincipal())
        .entityId(auditData.getEntityId())
        .entityClass(auditData.getEntityClass())
        .entityAction(auditData.getEntityAction())
        .details(auditData.getDetails())
        .build();
  }
}
