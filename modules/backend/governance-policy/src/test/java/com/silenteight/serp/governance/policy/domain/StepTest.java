package com.silenteight.serp.governance.policy.domain;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.USER;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.serp.governance.policy.domain.StepType.NARROW;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.*;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StepTest {

  private UUID stepId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");

  @Test
  void returnsFalseIfHasDifferentStepId() {
    // given
    UUID stepId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    Step step = getStep(stepId);

    // when
    boolean result = step.hasStepId(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));

    // then
    assertThat(result).isFalse();
  }

  @Test
  void returnsTrueIfHasTheSameStepId() {
    // given
    Step step = getStep(stepId);

    // when
    boolean result = step.hasStepId(stepId);

    // then
    assertThat(result).isTrue();
  }

  @NotNull
  private Step getStep(UUID stepId) {
    return new Step(SOLUTION_FALSE_POSITIVE, stepId, "step-name", "", BUSINESS_LOGIC, 0, "user");
  }

  @NotNull
  private Step getStep(UUID stepId, StepType type) {
    return new Step(
        SOLUTION_FALSE_POSITIVE,
        stepId,
        "step-name",
        "",
        type,
        0,
        "user");
  }

  @Test
  void shouldReturnTrueIfNarrowStep() {
    assertThat(getStep(stepId, NARROW).isNarrowStep()).isTrue();
    assertThat(getStep(stepId, BUSINESS_LOGIC).isNarrowStep()).isFalse();
  }

  @Test
  void shouldCloneStep() {
    //given
    Step originStep = createStep(FIRST_STEP_ID, 0);
    originStep.setFeatureLogics(of(FEATURE_LOGIC));

    //when
    Step clonedStep = originStep.cloneStep();

    //then
    assertNotEquals(originStep.getStepId(), clonedStep.getStepId());
    assertEquals(originStep.getName(), clonedStep.getName());
    assertEquals(originStep.getDescription(), clonedStep.getDescription());
    assertEquals(originStep.getSolution(), clonedStep.getSolution());
    assertEquals(originStep.getSortOrder(), clonedStep.getSortOrder());
    assertEquals(originStep.getType(), clonedStep.getType());
    assertEquals(
        originStep.getFeatureLogics().size(),
        clonedStep.getFeatureLogics().size());
    assertEquals(1, originStep.getFeatureLogics().size());
  }

  @Test
  void shouldCloneStepWithSpecificId() {
    //given
    Step originStep = createStep(FIRST_STEP_ID, 0);
    originStep.setFeatureLogics(of(FEATURE_LOGIC));

    //when
    Step clonedStep = originStep.cloneStep(SECOND_STEP_ID);

    //then
    assertNotEquals(originStep.getStepId(), clonedStep.getStepId());
    assertEquals(originStep.getName(), clonedStep.getName());
    assertEquals(originStep.getDescription(), clonedStep.getDescription());
    assertEquals(originStep.getSolution(), clonedStep.getSolution());
    assertEquals(originStep.getSortOrder(), clonedStep.getSortOrder());
    assertEquals(originStep.getType(), clonedStep.getType());
    assertEquals(
        originStep.getFeatureLogics().size(),
        clonedStep.getFeatureLogics().size());
    assertEquals(1, originStep.getFeatureLogics().size());
    assertThat(clonedStep.getStepId()).isEqualTo(SECOND_STEP_ID);
  }

  @Test
  void shouldCloneSteps() {
    //given
    List<Step> steps = of(
        createStep(FIRST_STEP_ID, 0),
        createStep(SECOND_STEP_ID, 0)
    );

    //when
    List<Step> clonedSteps = steps.stream().map(Step::cloneStep).collect(toList());

    //then
    assertThat(clonedSteps).hasSize(2);
  }

  @NotNull
  private Step createStep(UUID firstStep, int sortOrder) {
    return new Step(
        SOLUTION_FALSE_POSITIVE,
        firstStep,
        STEP_NAME,
        STEP_DESCRIPTION,
        BUSINESS_LOGIC,
        sortOrder,
        USER);
  }
}
