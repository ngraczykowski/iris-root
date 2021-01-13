package com.silenteight.serp.governance.policy.domain;

import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;
import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static java.util.Arrays.asList;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

class PolicyTest {

  @Test
  void returnsStepsInCorrectOrder() {
    // given
    Policy policy = new Policy(randomUUID(), "policy-name", "asmith");
    policy.setId(2L);
    UUID stepId1 = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    UUID stepId2 = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
    Step step1 = new Step(SOLUTION_FALSE_POSITIVE, stepId1, "step-name-1", "", BUSINESS_LOGIC, 0);
    Step step2 = new Step(SOLUTION_FALSE_POSITIVE, stepId2, "step-name-2", "", BUSINESS_LOGIC, 1);

    // when
    policy.addStep(step2);
    policy.addStep(step1);
    PolicyDto policyDto = policy.toDto();

    // then
    List<UUID> stepIds = toStepIds(policyDto.getSteps());
    assertThat(stepIds).isEqualTo(asList(stepId1, stepId2));
  }

  private static List<UUID> toStepIds(List<StepDto> steps) {
    return steps
        .stream()
        .map(StepDto::getId)
        .collect(toList());
  }
}
