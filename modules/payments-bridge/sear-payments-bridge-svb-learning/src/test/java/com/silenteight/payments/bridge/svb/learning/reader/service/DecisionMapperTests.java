package com.silenteight.payments.bridge.svb.learning.reader.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@Import({DecisionMapperConfiguration.class, DecisionMapperFactory.class})
class DecisionMapperTests {

  @Autowired private DecisionMapper decisionMapper;

  @Test
  void shouldTriggerCompoundPath() {
    assertEquals("compound",
        decisionMapper.map(List.of("L2_L3_PEND"), "L3_PASS"));
    assertEquals("compound",
        decisionMapper.map(List.of("L2_ESC_TO_L3", "OTHER"), "L3_PASS"));
    assertEquals("compound",
        decisionMapper.map(List.of("OTHER", "L2_ESC_TO_L3", "L2_L3_PEND"), "L3_PASS"));
    assertNotEquals("compound",
        decisionMapper.map(List.of("EXTERNAL"), "L3_PASS"));
  }

  @Test
  void shouldExecuteSimplePath() {
    assertEquals("simple",
        decisionMapper.map(List.of("FAKE_STATE"), "L3_PASS"));
    assertEquals("simple",
        decisionMapper.map(List.of(), "L2_CANCEL"));
    assertNotEquals("simple",
        decisionMapper.map(List.of(), "UNKNOWN"));
  }

  @Test
  void shouldExecuteDefaultPath() {
    assertEquals("default",
        decisionMapper.map(List.of("UNKNOWN_PREV_DECISION"), "UNKNOWN_DECISION"));
  }
}
