package com.silenteight.serp.governance.bulkchange.audit;

import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.proto.serp.v1.governance.BulkBranchChangeRejectedEvent;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static com.silenteight.protocol.utils.Uuids.toJavaUuid;
import static com.silenteight.serp.governance.bulkchange.BulkChangeCommands.BULK_CHANGE_ENTITY_CLASS_NAME;
import static java.lang.String.valueOf;

@RequiredArgsConstructor
class AuditBulkChangeRejectionHandler {

  private final AuditingLogger auditingLogger;

  void handle(BulkBranchChangeRejectedEvent auditEvent) {
    var auditDataDto = AuditDataDto.builder()
        .correlationId(toJavaUuid(auditEvent.getCorrelationId()))
        .eventId(UUID.randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .entityClass(BULK_CHANGE_ENTITY_CLASS_NAME)
        .entityId(valueOf(toJavaUuid(auditEvent.getId())))
        .entityAction("REJECT")
        .type("RejectBulkBranchChange")
        .build();

    auditingLogger.log(auditDataDto);
  }
}
