package com.silenteight.serp.governance.policy.domain.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.serp.governance.common.audit.AuditableRequest;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor(staticName = "of")
@Value
public class ConfigureStepLogicRequest implements AuditableRequest {

  UUID correlationId = UUID.randomUUID();
  @NonNull
  Long policyId;
  @NonNull
  UUID stepId;
  @NonNull
  Collection<FeatureLogicConfiguration> featureLogicConfigurations;
  @NonNull
  String editedBy;

  public Long getPolicyId() {
    return policyId;
  }

  public UUID getStepId() {
    return stepId;
  }

  public Collection<FeatureLogicConfiguration> getFeatureLogicConfigurations() {
    return featureLogicConfigurations;
  }

  public String getEditedBy() {
    return editedBy;
  }

  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto());
  }

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
        .entityId(stepId.toString())
        .entityClass("Step")
        .entityAction("UPDATE")
        .details(this.toString())
        .principal(editedBy)
        .build();
  }
}
