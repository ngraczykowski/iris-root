package com.silenteight.serp.governance.policy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.policy.domain.dto.*;
import com.silenteight.serp.governance.policy.domain.exception.*;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.serp.governance.policy.domain.PolicyState.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.time.Instant.now;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

@Entity
@Data
@Table(name = "governance_policy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Policy extends BaseAggregateRoot implements IdentifiableEntity {

  private static final String RESOURCE_NAME_PREFIX = "policies/";

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
        .id(getPolicyId())
        .name(asResourceName())
        .policyName(getName())
        .description(getDescription())
        .state(getState())
        .createdAt(getCreatedAt())
        .createdBy(getCreatedBy())
        .updatedAt(getLastModifyAt())
        .updatedBy(getUpdatedBy())
        .build();
  }

  private String asResourceName() {
    return RESOURCE_NAME_PREFIX + getPolicyId().toString();
  }

  public void addStep(Step step) {
    assertEditState();
    steps.add(step);
  }

  public void reconfigureStep(
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

  public void save() {
    assertAllowedStateChange(SAVED, DRAFT);
    setState(SAVED);
  }

  public void publish() {
    assertAllowedStateChange(TO_BE_USED, SAVED, TO_BE_USED, OBSOLETE, TO_BE_USED);
    setState(TO_BE_USED);
  }

  public boolean toBeUsed() {
    return state == TO_BE_USED;
  }

  public void use() {
    // TODO(kdzieciol): Remove `SAVED` state from the allowed states list (WEB-1092)
    assertAllowedStateChange(IN_USE, SAVED, TO_BE_USED, OBSOLETE);
    setState(IN_USE);
  }

  public void stopUsing() {
    assertAllowedStateChange(OBSOLETE, IN_USE);
    setState(OBSOLETE);
  }

  public void archive() {
    assertAllowedStateChange(ARCHIVED, SAVED, OBSOLETE);
    setState(ARCHIVED);
  }

  private void assertAllowedStateChange(PolicyState desirable, PolicyState... state) {
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

  private boolean notInState(PolicyState... allowedStates) {
    return stream(allowedStates).noneMatch(allowedState -> allowedState == getState());
  }

  public void updateStep(
      UUID stepId,
      String name,
      String description,
      FeatureVectorSolution solution,
      String editedBy) {

    assertEditState();

    Step step = getSteps()
        .stream()
        .filter(s -> s.hasStepId(stepId))
        .findFirst()
        .orElseThrow(() -> new StepNotFoundException(stepId));
    step.setUpdatedBy(editedBy);

    ofNullable(name).ifPresent(step::setName);
    ofNullable(description).ifPresent(step::setDescription);
    ofNullable(solution).ifPresent(step::setSolution);

    setUpdatedBy(editedBy);
  }

  public void deleteStep(UUID id) {
    assertEditState();
    getSteps().removeIf(s -> s.hasStepId(id));
  }

  public void updateStepsOrder(List<UUID> stepsOrder, String user) {
    assertEditState();
    assertSameSize(stepsOrder);
    assertSameUuids(stepsOrder);
    getSteps().forEach(step -> updateOrderOfStep(stepsOrder, user, step));
    assertStepOrderHierarchy();
    setUpdatedBy(user);
  }

  public Policy clonePolicy(UUID policyId, String createdBy) {
    return new Policy(
        policyId,
        getName(),
        getDescription(),
        createdBy);
  }

  private void assertSameSize(List<UUID> requestedOrder) {
    long dbStepsSize = getSteps().size();
    int requestStepsSize = requestedOrder.size();
    if (dbStepsSize != requestStepsSize)
      throw new StepsOrderListsSizeMismatch(getPolicyId(), requestStepsSize, dbStepsSize);
  }

  private void assertSameUuids(List<UUID> stepsOrder) {
    boolean notAllIdsCovered = getSteps()
        .stream()
        .anyMatch(step -> !stepsOrder.contains(step.getStepId()));

    if (notAllIdsCovered)
      throw new WrongIdsListInSetStepsOrder(getPolicyId());
  }

  private void updateOrderOfStep(List<UUID> stepsOrder, String user, Step step) {
    step.setSortOrder(stepsOrder.indexOf(step.getStepId()));
    step.setUpdatedBy(user);
  }

  void assertCanBeDeleted() {
    assertEditState();
  }

  boolean hasPolicyId(@NonNull UUID policyId) {
    return policyId.equals(getPolicyId());
  }

  private void assertStepOrderHierarchy() {
    int minSortOrderRegular = Integer.MAX_VALUE;
    int maxSortOrderNarrow = 0;
    for (Step step : steps) {
      if (step.isNarrowStep())
        maxSortOrderNarrow = max(step.getSortOrder(), maxSortOrderNarrow);
      else
        minSortOrderRegular = min(step.getSortOrder(), minSortOrderRegular);
    }

    if (maxSortOrderNarrow > minSortOrderRegular)
      throw new NarrowStepsOrderHierarchyMismatch(policyId);
  }

  TransferredPolicyRootDto toTransferablePolicyRootDto() {
    TransferredPolicyRootDto root = new TransferredPolicyRootDto();
    root.setMetadata(getTransferredMetadata());
    root.setPolicy(getTransferredPolicy());
    return root;
  }

  private TransferredPolicyMetadataDto getTransferredMetadata() {
    TransferredPolicyMetadataDto metadata = new TransferredPolicyMetadataDto();
    metadata.setCreatedAt(getCreatedAt().toInstant());
    metadata.setCreatedBy(getCreatedBy());
    metadata.setUpdatedAt(getUpdatedAt().toInstant());
    metadata.setUpdatedBy(getUpdatedBy());
    metadata.setExportedAt(now());
    return metadata;
  }

  private TransferredPolicyDto getTransferredPolicy() {
    TransferredPolicyDto dto = new TransferredPolicyDto();
    dto.setPolicyId(getPolicyId());
    dto.setName(getName());
    dto.setDescription(getDescription());
    dto.setSteps(getTransferredStepDtos());
    return dto;
  }

  private List<TransferredStepDto> getTransferredStepDtos() {
    return getSteps()
        .stream()
        .sorted(Step::compareTo)
        .map(Step::toTransferredDto)
        .collect(toList());
  }
}
