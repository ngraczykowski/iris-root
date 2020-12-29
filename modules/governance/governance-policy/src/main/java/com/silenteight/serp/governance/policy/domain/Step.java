package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.sep.base.common.entity.BaseModifiableEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "governance_policy_step")
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Step extends BaseModifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FeatureVectorSolution solution = FeatureVectorSolution.SOLUTION_NO_DECISION;

  @ToString.Include
  @Column(nullable = false)
  private UUID stepId;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  private String description;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StepType type = StepType.MANUAL_RULE;

  @OneToMany(cascade = ALL, orphanRemoval = true)
  @JoinColumn(name = "step_id", updatable = false, nullable = false)
  private Collection<FeatureLogic> featureLogics = new ArrayList<>();

  Step(
      FeatureVectorSolution solution, UUID stepId, String name, String description, StepType type) {

    this.solution = solution;
    this.stepId = stepId;
    this.name = name;
    this.description = description;
    this.type = type;
  }

  boolean hasStepId(UUID stepId) {
    return this.stepId.equals(stepId);
  }

  void reconfigure(Collection<FeatureLogic> featureLogics) {
    this.featureLogics.clear();
    this.featureLogics.addAll(featureLogics);
  }
}
