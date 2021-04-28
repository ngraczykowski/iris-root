package com.silenteight.serp.governance.model.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.serp.governance.model.common.ModelResource.toResourceName;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Model extends BaseAggregateRoot implements IdentifiableEntity {

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
        .name(toResourceName(getModelId()))
        .policy(getPolicyName())
        .createdAt(getCreatedAt())
        .build();
  }

  boolean hasModelId(UUID modelId) {
    return getModelId() == modelId;
  }

  boolean hasPolicyName(String policyName) {
    return getPolicyName().equals(policyName);
  }
}
