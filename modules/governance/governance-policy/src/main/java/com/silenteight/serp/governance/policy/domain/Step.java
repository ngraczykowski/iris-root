package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseModifiableEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.policy.domain.dto.*;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.serp.governance.policy.domain.StepType.NARROW;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "governance_policy_step")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Step extends BaseModifiableEntity
    implements IdentifiableEntity, StepSearchCriteriaMatcher, Comparable<Step> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FeatureVectorSolution solution = SOLUTION_NO_DECISION;

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
  private StepType type = BUSINESS_LOGIC;

  @ToString.Include
  @Column(nullable = false)
  private Integer sortOrder;

  @ToString.Include
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "updated_by", nullable = false)
  private String updatedBy;

  @OneToMany(cascade = ALL, orphanRemoval = true)
  @JoinColumn(name = "step_id", updatable = false, nullable = false)
  private Collection<FeatureLogic> featureLogics = new ArrayList<>();

  Step(
      FeatureVectorSolution solution,
      UUID stepId,
      String name,
      String description,
      StepType type,
      Integer sortOrder,
      String createdBy) {

    this.solution = solution;
    this.stepId = stepId;
    this.name = name;
    this.description = description;
    this.type = type;
    this.sortOrder = sortOrder;
    this.createdBy = createdBy;
    this.updatedBy = createdBy;
  }

  boolean hasStepId(UUID stepId) {
    return this.stepId.equals(stepId);
  }

  void reconfigure(Collection<FeatureLogic> featureLogics) {
    this.featureLogics.clear();
    this.featureLogics.addAll(featureLogics);
  }

  StepDto toDto() {
    return StepDto
        .builder()
        .id(getStepId())
        .solution(Solution.of(getSolution()))
        .name(getName())
        .description(getDescription())
        .type(getType())
        .createdBy(getCreatedBy())
        .createdAt(getCreatedAt())
        .updatedBy(getUpdatedBy())
        .updatedAt(getLastModifyAt())
        .build();
  }

  StepConfigurationDto toConfigurationDto() {
    return StepConfigurationDto
        .builder()
        .id(getStepId())
        .title(getName())
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

  public boolean isNarrowStep() {
    return type == NARROW;
  }

  TransferredStepDto toTransferredDto() {
    TransferredStepDto dto = new TransferredStepDto();
    dto.setStepId(getStepId());
    dto.setName(getName());
    dto.setDescription(getDescription());
    dto.setType(getType());
    dto.setSolution(getSolution());
    dto.setFeatureLogics(toTransferredFeatureLogicDtos());
    return dto;
  }

  private List<TransferredFeatureLogicDto> toTransferredFeatureLogicDtos() {
    return getFeatureLogics()
        .stream()
        .map(FeatureLogic::toTransferredDto)
        .collect(toList());
  }

  public Step cloneStep() {
    return cloneStep(randomUUID());
  }

  public Step cloneStep(UUID stepId) {
    Step step = new Step(
        getSolution(),
        stepId,
        getName(),
        getDescription(),
        getType(),
        getSortOrder(),
        getCreatedBy());

    List<FeatureLogic> clonedFeatureLogic = cloneFeatureLogics(this.getFeatureLogics());
    step.setFeatureLogics(clonedFeatureLogic);
    return step;
  }

  @Override
  public int compareTo(@NotNull Step step) {
    return this.getSortOrder().compareTo(step.getSortOrder());
  }

  List<FeatureLogic> cloneFeatureLogics(Collection<FeatureLogic> featureLogics) {
    return featureLogics
        .stream()
        .map(FeatureLogic::cloneFeatureLogic)
        .collect(toList());
  }

  @Override
  public boolean match(StepSearchCriteriaDto criteria) {
    return featureLogics.stream().anyMatch(fl -> fl.match(criteria));
  }

  public void incrementOrder() {
    ++sortOrder;
  }
}
