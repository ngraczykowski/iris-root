package com.silenteight.agent.facade;

import com.silenteight.agents.v1.api.exchange.AgentOutput;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

class AgentFacadeProcessorTest {

  @Test
  void shouldRunWorker() {
    var agentOutput = AgentOutput.newBuilder().build();
    var underTest = new AgentFacadeProcessor<>((x, s) -> agentOutput);
    var agentInput = mock(AgentInput.class);
    var result = underTest.process(List.of(agentInput), Set.of("configName"));
    assertEquals(result.getAgentOutputs(0), agentOutput);
  }
}
