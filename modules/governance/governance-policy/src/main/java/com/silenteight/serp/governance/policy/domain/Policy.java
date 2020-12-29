package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "updated_by", nullable = false)
  private String updatedBy;

  @OneToMany(cascade = ALL, orphanRemoval = true)
  @JoinColumn(name = "policy_id", updatable = false, nullable = false)
  private Collection<Step> steps = new ArrayList<>();

  Policy(UUID policyId, String name, String createdBy) {
    this.policyId = policyId;
    this.name = name;
    this.createdBy = createdBy;
    this.updatedBy = createdBy;
  }

  void addStep(Step step) {
    steps.add(step);
  }

  void reconfigureStep(UUID stepId, Collection<FeatureLogic> featureLogics) {
    Step step = steps
        .stream()
        .filter(s -> s.hasStepId(stepId))
        .findFirst()
        .orElseThrow(() -> new StepNotFoundException(stepId));
    step.reconfigure(featureLogics);
  }
}
