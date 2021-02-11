package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class AddPolicyRequest implements AuditableRequest {

  @NonNull
  UUID correlationId;
  @NonNull
  UUID policyId;
  @NonNull
  String policyName;
  @Nullable
  String description;
  @NonNull
  String createdBy;

  public static AddPolicyRequest of(
      UUID policyId, String policyName, String description, String createdBy) {
    return AddPolicyRequest.of(randomUUID(), policyId,policyName, description,createdBy);
  }

  @Override
  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto());
  }

  @Override
  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto());
  }

  private AuditDataDto getAuditDataDto() {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type(this.getClass().getSimpleName())
        .entityId(policyId.toString())
        .entityClass("Policy")
        .entityAction("CREATE")
        .details(this.toString())
        .principal(createdBy)
.build();
  }
}
