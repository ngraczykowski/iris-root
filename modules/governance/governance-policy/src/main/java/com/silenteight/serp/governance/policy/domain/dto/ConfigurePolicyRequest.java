package com.silenteight.serp.governance.policy.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.governance.api.v1.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.domain.StepType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

@Value
@Builder
public class ConfigurePolicyRequest implements AuditableRequest {

  UUID correlationId = UUID.randomUUID();
  UUID policyId = randomUUID();
  @NonNull
  String policyName;

  String description;

  @NonNull
  String createdBy;

  @NonNull
  List<StepConfiguration> stepConfigurations;

  @Value
  @Builder
  public static class StepConfiguration {

    @NonNull
    FeatureVectorSolution solution;

    @NonNull
    String stepName;

    String stepDescription;

    @NonNull
    StepType stepType;

    @NonNull
    Collection<FeatureLogicConfiguration> featureLogicConfigurations;
  }

  @Value
  @Builder
  public static class FeatureLogicConfiguration {

    int toFulfill;

    @NonNull
    Collection<FeatureConfiguration> featureConfigurations;
  }

  @Value
  @Builder
  public static class FeatureConfiguration {

    @NonNull
    String name;

    @NonNull
    Condition condition;

    @NonNull
    Collection<String> values;
  }

  public void preAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto("PolicyImportRequest"));
  }

  public void postAudit(Consumer<AuditDataDto> logger) {
    logger.accept(getAuditDataDto("PolicyImported"));
  }

  private AuditDataDto getAuditDataDto(String type) {
    return AuditDataDto
        .builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type(type)
        .entityId(policyId.toString())
        .entityClass("Policy")
        .entityAction("CREATE")
        .details(this.toString())
        .principal(createdBy)
.build();
  }
}
