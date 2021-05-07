package com.silenteight.serp.governance.agent;

import com.silenteight.serp.governance.agent.config.AgentDiscovery;
import com.silenteight.serp.governance.agent.details.AgentDetailsClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.silenteight.serp.governance.agent.AgentFixture.DATE_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.NAME_AGENT;
import static com.silenteight.serp.governance.agent.config.AgentConfigFixture.DATE_AGENT_CONFIG;
import static com.silenteight.serp.governance.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG;
import static com.silenteight.serp.governance.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.DATE_AGENT_DETAIL;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.NAME_AGENT_DETAIL;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentsRegistryTest {

  @Mock
  AgentDiscovery agentDiscovery;

  @Mock
  AgentDetailsClient agentDetailsClient;

  @InjectMocks
  AgentsRegistry underTest;

  @Test
  void shouldReturnAllAgents() {
    when(agentDiscovery.getAgentConfigs())
        .thenReturn(of(NAME_AGENT_CONFIG, DATE_AGENT_CONFIG));
    when(agentDetailsClient.getAgentDetailsForAgentConfig(NAME_AGENT_CONFIG))
        .thenReturn(NAME_AGENT_DETAIL);
    when(agentDetailsClient.getAgentDetailsForAgentConfig(DATE_AGENT_CONFIG))
        .thenReturn(DATE_AGENT_DETAIL);

    List<AgentDto> agents = underTest.getAllAgents();

    assertThat(agents).containsExactlyInAnyOrder(NAME_AGENT, DATE_AGENT);
  }

  @Test
  void shouldReturnSpecificAgent() {
    when(agentDiscovery.getAgentConfigs()).thenReturn(of(NAME_AGENT_CONFIG));
    when(agentDetailsClient.getAgentDetailsForAgentConfig(NAME_AGENT_CONFIG))
        .thenReturn(NAME_AGENT_DETAIL);

    Optional<AgentDto> singleAgent = underTest.getSingleAgent(NAME_AGENT_CONFIG_NAME);

    assertThat(singleAgent).get().isEqualTo(NAME_AGENT);
  }

  @Test
  void shouldPropagateExceptionIfAgentConfigurationIsNotPresent() {
    when(agentDiscovery.getAgentConfigs()).thenReturn(of(NAME_AGENT_CONFIG));
    when(agentDetailsClient.getAgentDetailsForAgentConfig(NAME_AGENT_CONFIG))
        .thenThrow(NonResolvableResourceException.class);

    assertThatThrownBy(() -> underTest.getAllAgents())
        .isInstanceOf(NonResolvableResourceException.class);
  }
}
