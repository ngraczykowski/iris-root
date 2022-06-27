package com.silenteight.sens.webapp.audit.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

interface AuditLogRepository extends Repository<AuditLog, UUID> {

  AuditLog save(AuditLog auditLog);

  Collection<AuditLog> findAllByTimestampBetween(OffsetDateTime from, OffsetDateTime to);
}
