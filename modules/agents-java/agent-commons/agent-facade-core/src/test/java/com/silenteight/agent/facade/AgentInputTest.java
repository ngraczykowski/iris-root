package com.silenteight.agent.facade;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class AgentInputTest {

  AgentInput agentInput;

  @BeforeEach
  void setUp() {
    agentInput = new AgentInput() {
      @Override
      public String getMatch() {
        return "test";
      }

      @Override
      public List<? extends AgentFeatureInput> getFeatureInputs() {
        return List.of(() -> "feature1", () -> "feature2");
      }
    };
  }

  @Test
  void shouldGetFeatureInputsAsString() {
    //when
    var result = agentInput.getFeatureInputsAsString();

    //then
    Assertions.assertThat(result).isEqualTo("feature1,feature2");
  }
}
