package com.silenteight.serp.governance.policy.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

class StepTest {

  @Test
  void returnsFalseIfHasDifferentStepId() {
    // given
    UUID stepId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    Step step = new Step(SOLUTION_FALSE_POSITIVE, stepId, "step-name", "", BUSINESS_LOGIC, 0);

    // when
    boolean result = step.hasStepId(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"));

    // then
    assertThat(result).isFalse();
  }

  @Test
  void returnsTrueIfHasTheSameStepId() {
    // given
    UUID stepId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    Step step = new Step(SOLUTION_FALSE_POSITIVE, stepId, "step-name", "", BUSINESS_LOGIC, 0);

    // when
    boolean result = step.hasStepId(stepId);

    // then
    assertThat(result).isTrue();
  }
}
