package com.silenteight.simulator.dataset.archive;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.simulator.common.audit.AuditableRequest;

import java.util.UUID;
import java.util.function.Consumer;

import static java.sql.Timestamp.from;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Value
@Builder
public class ArchiveDatasetRequest implements AuditableRequest {

  static final String PRE_AUDIT_TYPE = "ArchiveDatasetRequested";
  static final String POST_AUDIT_TYPE = "DatasetArchived";

  @NonNull
  UUID id;
  @NonNull
  String archivedBy;
  @Builder.Default
  UUID correlationId = randomUUID();

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(PRE_AUDIT_TYPE));
  }

  @Override
  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(POST_AUDIT_TYPE));
  }

  private AuditDataDto getAuditDataDto(String type) {
    return AuditDataDto.builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(from(now()))
        .type(type)
        .entityId(id.toString())
        .entityClass("Dataset")
        .entityAction("UPDATE")
        .details(toString())
        .principal(archivedBy)
        .build();
  }
}
