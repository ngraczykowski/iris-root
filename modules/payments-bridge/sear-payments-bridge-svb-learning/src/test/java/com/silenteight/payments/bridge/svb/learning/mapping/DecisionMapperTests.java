package com.silenteight.payments.bridge.svb.learning.mapping;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@Import({ DecisionMapperConfiguration.class, DecisionMapperFactory.class })
class DecisionMapperTests {

  @Autowired private DecisionMapper decisionMapper;

  @Test
  void shouldTriggerCompoundPath() {
    assertEquals(
        "analyst_decision_true_positive",
        decisionMapper.map(List.of("L2_L3_PEND"), "L3_PASS"));
    assertEquals(
        "analyst_decision_true_positive",
        decisionMapper.map(List.of("L2_ESC_TO_L3", "OTHER"), "L3_PASS"));
    assertEquals(
        "analyst_decision_true_positive",
        decisionMapper.map(List.of("OTHER", "L2_ESC_TO_L3", "L2_L3_PEND"), "L3_PASS"));
    assertNotEquals(
        "analyst_decision_potential_true_positive",
        decisionMapper.map(List.of("EXTERNAL"), "L3_PASS"));
  }

  @Test
  void shouldExecuteSimplePath() {
    assertEquals(
        "analyst_decision_false_positive",
        decisionMapper.map(List.of("FAKE_STATE"), "L3_PASS"));
    assertEquals(
        "analyst_decision_true_positive",
        decisionMapper.map(List.of(), "L2_CANCEL"));
    assertNotEquals(
        "analyst_decision_false_positive",
        decisionMapper.map(List.of(), "UNKNOWN"));
  }

  @Test
  void shouldExecuteDefaultPath() {
    assertEquals(
        "analyst_decision_unknown",
        decisionMapper.map(List.of("UNKNOWN_PREV_DECISION"), "UNKNOWN_DECISION"));
  }
}
