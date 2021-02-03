package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.governance.api.v1.FeatureVectorSolution;
import com.silenteight.sep.base.common.entity.BaseModifiableEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.Solution;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "governance_policy_step")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Step extends BaseModifiableEntity implements IdentifiableEntity {

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

  @ToString.Include
  @Column(nullable = false)
  private Integer sortOrder;

  @OneToMany(cascade = ALL, orphanRemoval = true)
  @JoinColumn(name = "step_id", updatable = false, nullable = false)
  private Collection<FeatureLogic> featureLogics = new ArrayList<>();

  Step(
      FeatureVectorSolution solution,
      UUID stepId,
      String name,
      String description,
      StepType type,
      Integer sortOrder) {

    this.solution = solution;
    this.stepId = stepId;
    this.name = name;
    this.description = description;
    this.type = type;
    this.sortOrder = sortOrder;
  }

  boolean hasStepId(UUID stepId) {
    return this.stepId.equals(stepId);
  }

  void reconfigure(Collection<FeatureLogic> featureLogics) {
    this.featureLogics.clear();
    this.featureLogics.addAll(featureLogics);
  }

  StepDto toDto() {
    return StepDto.builder()
        .id(getStepId())
        .solution(Solution.of(getSolution()))
        .name(getName())
        .description(getDescription())
        .type(getType())
        .build();
  }

  StepConfigurationDto toConfigurationDto() {
    return StepConfigurationDto.builder()
        .id(getStepId())
        .solution(getSolution())
        .featureLogics(featureLogicsToConfigurationDto())
        .build();
  }

  private Collection<FeatureLogicConfigurationDto> featureLogicsToConfigurationDto() {
    return getFeatureLogics()
        .stream()
        .map(FeatureLogic::toConfigurationDto)
        .collect(toList());
  }
}
