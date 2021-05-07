package com.silenteight.serp.governance.agent.details;

import com.silenteight.serp.governance.agent.TestResourceLoader;
import com.silenteight.serp.governance.agent.config.AgentConfigDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static com.silenteight.serp.governance.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.AGENT_FEATURE_NAME;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.AGENT_RESPONSE_MATCH;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.AGENT_RESPONSE_NO_DATA;
import static org.assertj.core.api.Assertions.*;

class AgentDetailsClientTest {

  private static final String SAMPLE_INPUT = "{"
      + "  \"" + NAME_AGENT_CONFIG_NAME + "\": {"
      + "    \"features\": ["
      + "      \"" + AGENT_FEATURE_NAME + "\""
      + "    ],"
      + "    \"responses\": ["
      + "      \"" + AGENT_RESPONSE_MATCH + "\","
      + "      \"" + AGENT_RESPONSE_NO_DATA + "\""
      + "    ]"
      + "  }"
      + "}";

  private static final String SOURCE = "source/to/file";

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shouldReturnAgentDetailsForSpecificAgentConfig() {
    AgentDetailsClient underTest =
        new AgentDetailsClient(SOURCE, objectMapper, new TestResourceLoader(SAMPLE_INPUT));

    underTest.init();
    AgentDetailDto agentDetailsDto = underTest.getAgentDetailsForAgentConfig(
        new AgentConfigDto(NAME_AGENT_CONFIG_NAME));

    assertThat(agentDetailsDto.getFeatures())
        .containsExactlyInAnyOrder(AGENT_FEATURE_NAME);
    assertThat(agentDetailsDto.getResponses())
        .containsExactlyInAnyOrder(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_NO_DATA);
  }

  @Test
  void shouldThrowExceptionIfConfigNotPresent() {
    AgentDetailsClient underTest =
        new AgentDetailsClient(SOURCE, objectMapper, new TestResourceLoader(null));
    AgentConfigDto agentConfigDto = new AgentConfigDto(NAME_AGENT_CONFIG_NAME);

    underTest.init();
    assertThatThrownBy(() -> underTest.getAgentDetailsForAgentConfig(agentConfigDto))
        .isInstanceOf(UnreachableAgentException.class);
  }
}
