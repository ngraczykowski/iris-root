package com.silenteight.serp.governance.agent.config;

import com.silenteight.serp.governance.agent.TestResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.silenteight.serp.governance.agent.config.AgentConfigFixture.DATE_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static org.assertj.core.api.Assertions.*;

class AgentDiscoveryTest {

  private static final String SAMPLE_INPUT = "{"
      + "  \"agentConfigs\": ["
      + "    { \"name\": \"" + NAME_AGENT_CONFIG_NAME + "\" },"
      + "    { \"name\": \"" + DATE_AGENT_CONFIG_NAME + "\" }"
      + "  ]"
      + "}";

  private static final String SOURCE = "source/to/file";

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shouldLoadAgentConfigFromJson() {
    AgentDiscovery underTest =
        new AgentDiscovery(SOURCE, objectMapper, new TestResourceLoader(SAMPLE_INPUT));

    underTest.loadAgents();
    Set<AgentConfigDto> agentConfigDtos = underTest.getAgentConfigs();

    assertThat(agentConfigDtos).containsExactlyInAnyOrder(
        new AgentConfigDto(NAME_AGENT_CONFIG_NAME),
        new AgentConfigDto(DATE_AGENT_CONFIG_NAME)
    );
  }

  @Test
  void shouldThrowExceptionAtStartupIfConfigNotPresent() {
    AgentDiscovery underTest =
        new AgentDiscovery(SOURCE, objectMapper, new TestResourceLoader(null));

    assertThatThrownBy(underTest::loadAgents)
        .isInstanceOf(AgentDiscoveryException.class);
  }
}
