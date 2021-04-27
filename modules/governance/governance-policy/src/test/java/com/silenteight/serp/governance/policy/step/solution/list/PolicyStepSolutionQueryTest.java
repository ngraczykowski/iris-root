package com.silenteight.serp.governance.policy.step.solution.list;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PolicyStepSolutionQueryTest {

  @Test
  void listSolutionsWhenHintedIsDisabled() {
    // given
    boolean hintedEnabled = false;
    PolicyStepSolutionQuery query = new PolicyStepSolutionQuery(hintedEnabled);

    // when
    List<String> solutions = query.list();

    // then
    assertThat(solutions).containsExactlyInAnyOrder(
        "NO_DECISION", "FALSE_POSITIVE", "POTENTIAL_TRUE_POSITIVE");
  }

  @Test
  void listSolutionsWhenHintedIsEnabled() {
    // given
    boolean hintedEnabled = true;
    PolicyStepSolutionQuery query = new PolicyStepSolutionQuery(hintedEnabled);

    // when
    List<String> solutions = query.list();

    // then
    assertThat(solutions).containsExactlyInAnyOrder(
        "NO_DECISION",
        "FALSE_POSITIVE",
        "HINTED_FALSE_POSITIVE",
        "POTENTIAL_TRUE_POSITIVE",
        "HINTED_POTENTIAL_TRUE_POSITIVE");
  }
}
