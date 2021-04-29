package com.silenteight.simulator.management;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.simulator.common.audit.AuditableRequest;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static java.sql.Timestamp.from;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@Value
@Builder
public class CreateSimulationRequest implements AuditableRequest {

  static final String PRE_AUDIT_TYPE = "CreateSimulationRequested";
  static final String POST_AUDIT_TYPE = "SimulationCreated";

  @NonNull
  UUID id;
  @NonNull
  String simulationName;
  String description;
  @NonNull
  String createdBy;
  @NonNull
  Set<String> datasets;
  @NonNull
  String model;
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
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(from(now()))
        .type(type)
        .entityId(id.toString())
        .entityClass("Simulation")
        .entityAction("CREATE")
        .details(toString())
        .principal(createdBy)
        .build();
  }
}
