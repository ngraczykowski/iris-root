package com.silenteight.simulator.management;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.simulator.common.audit.AuditableRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

@Value
@Builder
public class CreateSimulationRequest implements AuditableRequest {

  static final String PRE_AUDIT_TYPE = "CreateSimulationRequest";
  static final String POST_AUDIT_TYPE = "SimulationCreated";

  @NonNull
  UUID id;

  @NonNull
  String name;

  String description;

  @NonNull
  String createdBy;

  @NonNull
  UUID datasetId;

  @NonNull
  UUID policyId;

  @Builder.Default
  UUID correlationId = UUID.randomUUID();

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(PRE_AUDIT_TYPE));
  }

  @Override
  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto(POST_AUDIT_TYPE));
  }

  private AuditDataDto getAuditDataDto(String type) {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type(type)
        .entityId(id.toString())
        .entityClass("Simulation")
        .entityAction("CREATE")
        .details(this.toString())
        .principal(createdBy)
        .build();
  }
}