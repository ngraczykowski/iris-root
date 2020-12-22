package com.silenteight.serp.governance.bulkchange.audit;

import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeAppliedEvent;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static com.silenteight.protocol.utils.Uuids.toJavaUuid;
import static com.silenteight.serp.governance.bulkchange.BulkChangeCommands.BULK_CHANGE_ENTITY_CLASS_NAME;
import static java.lang.String.valueOf;

@RequiredArgsConstructor
class AuditBulkChangeApplicationHandler {

  private final AuditingLogger auditingLogger;

  void handle(BulkBranchChangeAppliedEvent auditEvent) {
    var auditDataDto = AuditDataDto.builder()
        .correlationId(toJavaUuid(auditEvent.getCorrelationId()))
        .eventId(UUID.randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .entityClass(BULK_CHANGE_ENTITY_CLASS_NAME)
        .entityId(valueOf(toJavaUuid(auditEvent.getId())))
        .entityAction("APPLY")
        .type("ApplyBulkBranchChange")
        .build();

    auditingLogger.log(auditDataDto);
  }
}
