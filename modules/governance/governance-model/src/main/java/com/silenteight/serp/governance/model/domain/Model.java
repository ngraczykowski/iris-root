package com.silenteight.serp.governance.model.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import java.util.UUID;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Model extends BaseAggregateRoot implements IdentifiableEntity {

  private static final String RESOURCE_NAME_PREFIX = "models/";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @NonNull
  @ToString.Include
  @Column(name = "model_id", nullable = false)
  private UUID modelId;

  @NonNull
  @ToString.Include
  @Column(name = "policy_name", nullable = false)
  private String policyName;

  Model(UUID modelId, String policyName) {
    this.modelId = modelId;
    this.policyName = policyName;
  }

  ModelDto toDto() {
    return ModelDto.builder()
        .id(getModelId())
        .name(asResourceName())
        .policyName(getPolicyName())
        .createdAt(getCreatedAt())
        .build();
  }

  private String asResourceName() {
    return RESOURCE_NAME_PREFIX + getModelId().toString();
  }

  boolean hasModelId(UUID modelId) {
    return getModelId() == modelId;
  }

  boolean hasPolicyName(String policyName) {
    return getPolicyName().equals(policyName);
  }
}
