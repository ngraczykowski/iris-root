package com.silenteight.agent.facade;

import com.silenteight.agent.facade.datasource.AmqpMessageToDataSourceRequestMapper;
import com.silenteight.agent.facade.datasource.DataSourceClient;
import com.silenteight.agent.facade.exchange.AgentFacadeProperties;
import com.silenteight.agent.monitoring.Monitoring;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentOutput;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractAgentFacadeTest {

  @Mock
  private DataSourceClient<Object, AgentInput> dataSourceClient;
  @Mock
  private AmqpMessageToDataSourceRequestMapper<Object> mapper;
  @Mock
  private Monitoring monitoring;

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 })
  void shouldProcessMessage(int parallelism) {
    var request = AgentExchangeRequest.newBuilder().build();
    var command = new Object();
    var agentOutput = AgentOutput.newBuilder().build();
    var agentInput = mock(AgentInput.class);
    var underTest = new AbstractAgentFacade<>(dataSourceClient, mapper, monitoring) {
      @Override
      public AgentOutput getAgentResponsesForMatch(
          AgentInput agentInput) {
        return agentOutput;
      }
    };
    doReturn(command).when(mapper).map(eq(request));
    doReturn(List.of(agentInput)).when(dataSourceClient).getMatchInputs(eq(command));
    AgentFacadeProperties props = new AgentFacadeProperties();
    props.setParallelism(parallelism);
    underTest.configure(props);

    var result = underTest.processMessage(request);

    assertEquals(result.getAgentOutputsList().get(0), agentOutput);
  }
}