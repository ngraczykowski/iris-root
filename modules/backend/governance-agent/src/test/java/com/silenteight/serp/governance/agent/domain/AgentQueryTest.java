package com.silenteight.serp.governance.agent.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.silenteight.serp.governance.agent.AgentFixture.DATE_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.NAME_AGENT;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.DETAILS_DATE_AGENT_DTO;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.DATE_AGENT_DETAIL_DTO;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.DATE_AGENT_ID;
import static com.silenteight.serp.governance.agent.list.ListAgentFixture.LIST_AGENT_DATE_DTO;
import static com.silenteight.serp.governance.agent.list.ListAgentFixture.LIST_AGENT_NAME_DTO;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgentQueryTest {

  AgentQuery underTest;

  AgentsRegistry agentsRegistry;

  @BeforeEach
  public void setUp() {
    agentsRegistry = Mockito.mock(AgentsRegistry.class);
    underTest = new AgentQuery(agentsRegistry);
  }

  @Test
  void shouldReturnAgentDtoList() {
    when(agentsRegistry.getAllAgents()).thenReturn(of(NAME_AGENT, DATE_AGENT));
    assertThat(underTest.list())
        .containsExactlyInAnyOrder(LIST_AGENT_NAME_DTO, LIST_AGENT_DATE_DTO);
    verify(agentsRegistry, times(1)).getAllAgents();
  }

  @Test
  void shouldReturnAgentDetails() {
    when(agentsRegistry.getSingleAgentById(DATE_AGENT_ID)).thenReturn(DATE_AGENT_DETAIL_DTO);
    assertThat(underTest.details(DATE_AGENT_ID)).isEqualTo(DETAILS_DATE_AGENT_DTO);
    verify(agentsRegistry, times(1)).getSingleAgentById(DATE_AGENT_ID);
  }
}
