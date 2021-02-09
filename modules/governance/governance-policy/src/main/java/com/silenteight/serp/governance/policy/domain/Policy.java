package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;
import com.silenteight.serp.governance.policy.domain.exception.StepNotFoundException;
import com.silenteight.serp.governance.policy.domain.exception.WrongPolicyStateChangeException;
import com.silenteight.serp.governance.policy.domain.exception.WrongPolicyStateException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.serp.governance.policy.domain.PolicyState.DRAFT;
import static com.silenteight.serp.governance.policy.domain.PolicyState.IN_USE;
import static com.silenteight.serp.governance.policy.domain.PolicyState.SAVED;
import static javax.persistence.CascadeType.ALL;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Policy extends BaseAggregateRoot implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(nullable = false)
  private UUID policyId;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  private String description;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PolicyState state;

  @ToString.Include
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "updated_by", nullable = false)
  private String updatedBy;

  @OneToMany(cascade = ALL, orphanRemoval = true)
  @JoinColumn(name = "policy_id", updatable = false, nullable = false)
  private Collection<Step> steps = new ArrayList<>();

  Policy(UUID policyId, String name, String description, String createdBy) {
    this.policyId = policyId;
    this.name = name;
    this.description = description;
    this.createdBy = createdBy;
    this.updatedBy = createdBy;
    this.state = DRAFT;
  }

  PolicyDto toDto() {
    return PolicyDto.builder()
        .policyId(getId())
        .name(getName())
        .id(getPolicyId())
        .state(getState())
        .description(getDescription())
        .createdAt(getCreatedAt())
        .createdBy(getCreatedBy())
        .updatedAt(getLastModifyAt())
        .updatedBy(getUpdatedBy())
        .build();
  }

  void addStep(Step step) {
    steps.add(step);
  }

  void reconfigureStep(
      @NonNull UUID stepId,
      @NonNull Collection<FeatureLogic> featureLogics,
      @NonNull String editedBy) {

    assertEditState();
    
    Step step = steps
        .stream()
        .filter(s -> s.hasStepId(stepId))
        .findFirst()
        .orElseThrow(() -> new StepNotFoundException(stepId));
    step.setUpdatedBy(editedBy);
    step.reconfigure(featureLogics);
    setUpdatedBy(editedBy);
  }

  void save() {
    assertAllowedStateChange(DRAFT, SAVED);
    setState(SAVED);
  }

  void use() {
    assertAllowedStateChange(SAVED, IN_USE);
    setState(IN_USE);
  }

  void stopUsing() {
    setState(SAVED);
  }

  private void assertAllowedStateChange(PolicyState state, PolicyState desirable) {
    if (notInState(state))
      throw new WrongPolicyStateChangeException(getPolicyId(), getState(), desirable);
  }

  public void setName(String name) {
    assertEditState();
    this.name = name;
  }

  public void setDescription(String description) {
    assertEditState();
    this.description = description;
  }

  void assertEditState() {
    if (notInState(DRAFT))
      throw new WrongPolicyStateException(getPolicyId(), getState());
  }

  private boolean notInState(PolicyState state) {
    return getState() != state;
  }
}
