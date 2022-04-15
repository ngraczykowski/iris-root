package com.silenteight.serp.governance.agent.domain.file.details;

import com.silenteight.serp.governance.agent.TestResourceLoader;
import com.silenteight.serp.governance.agent.domain.file.config.AgentConfigDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static com.silenteight.serp.governance.agent.domain.file.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_ENT_NORMAL;
import static com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsFixture.AGENT_CONF_DATE_ENT_NORMAL_FILE;
import static com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsFixture.*;
import static org.assertj.core.api.Assertions.*;

class AgentDetailsClientTest {

  private static final String SAMPLE_INPUT = "{"
      + "  \"" + NAME_AGENT_CONFIG_NAME + "\": {"
      + "    \"agentId\": \"" + NAME_AGENT_ID + "\","
      + "    \"features\": ["
      + "      \"" + AGENT_FEATURE_NAME + "\""
      + "    ],"
      + "    \"featuresList\": ["
      + "      {"
      + "         \"name\": \"" + AGENT_FEATURE_NAME + "\","
      + "         \"displayName\": \"" + AGENT_FEATURE_NAME_DISPLAY_NAME + "\""
      + "       },"
      + "      {"
      + "         \"name\": \"" + AGENT_FEATURE_DATE + "\","
      + "         \"displayName\": \"" + AGENT_FEATURE_DATE_DISPLAY_NAME + "\""
      + "       }"
      + "    ],"
      + "    \"responses\": ["
      + "      \"" + AGENT_RESPONSE_MATCH + "\","
      + "      \"" + AGENT_RESPONSE_NO_DATA + "\""
      + "    ],"
      + "    \"configurations\": ["
      + "      {"
      + "         \"name\": \"" + AGENT_CONF_DATE_ENT_NORMAL + "\","
      + "         \"configFile\": \"" + AGENT_CONF_DATE_ENT_NORMAL_FILE + "\""
      + "       }"
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

    assertThat((agentDetailsDto.getFeaturesList())).hasSize(2);
    assertThat(agentDetailsDto.getFeaturesList().get(0).getName())
        .isEqualTo(AGENT_FEATURE_NAME);

    assertThat(agentDetailsDto.getFeaturesList().get(0).getDisplayName())
        .isEqualTo(AGENT_FEATURE_NAME_DISPLAY_NAME);

    assertThat(agentDetailsDto.getResponses())
        .containsExactlyInAnyOrder(AGENT_RESPONSE_MATCH, AGENT_RESPONSE_NO_DATA);
  }

  @Test
  void shouldThrowExceptionIfConfigNotPresent() {
    AgentDetailsClient underTest =
        new AgentDetailsClient(SOURCE, objectMapper, new TestResourceLoader(""));
    AgentConfigDto agentConfigDto = new AgentConfigDto(NAME_AGENT_CONFIG_NAME);

    underTest.init();
    assertThatThrownBy(() -> underTest.getAgentDetailsForAgentConfig(agentConfigDto))
        .isInstanceOf(UnreachableAgentException.class);
  }
}
