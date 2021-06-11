package com.silenteight.simulator.dataset.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.simulator.common.audit.AuditableRequest;
import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static java.sql.Timestamp.from;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Value
@Builder
public class CreateDatasetRequest implements AuditableRequest {

  static final String PRE_AUDIT_TYPE = "CreateDatasetRequested";
  static final String POST_AUDIT_TYPE = "DatasetCreated";

  @NonNull
  UUID id;
  @NonNull
  String datasetName;
  String description;
  @NonNull
  String createdBy;
  @NonNull
  AlertSelectionCriteriaDto query;
  @Builder.Default
  UUID correlationId = randomUUID();

  public OffsetDateTime getRangeFrom() {
    return query.getRangeFrom();
  }

  public OffsetDateTime getRangeTo() {
    return query.getRangeTo();
  }

  public List<String> getCountries() {
    return query.getCountries();
  }

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
        .entityAction("CREATE")
        .details(toString())
        .principal(createdBy)
        .build();
  }
}
