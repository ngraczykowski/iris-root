package com.silenteight.serp.governance.model.featureset;

import com.silenteight.serp.governance.model.TestResourceLoader;
import com.silenteight.serp.governance.model.agent.AgentsRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.serp.governance.model.agent.AgentFixture.NAME_AGENT;
import static com.silenteight.serp.governance.model.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.model.agent.details.AgentDetailsFixture.AGENT_FEATURE_NAME;
import static com.silenteight.serp.governance.model.featureset.FeatureSetRegistry.DEFAULT_FEATURE_SET;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureSetRegistryTest {

  private static final String SAMPLE_CONFIG = "["
      + "  {"
      + "    \"name\" : \"" + DEFAULT_FEATURE_SET + "\","
      + "    \"features\": [{"
      + "      \"name\": \"" + AGENT_FEATURE_NAME + "\","
      + "      \"agentConfig\": \"" + NAME_AGENT_CONFIG_NAME + "\""
      + "    }]"
      + "  }"
      + "]";

  private static final String SOURCE = "source/to/file";

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Mock
  AgentsRegistry agentsRegistry;

  FeatureSetRegistry underTest;

  @BeforeEach
  void init() {
    TestResourceLoader resourceLoader = new TestResourceLoader(SAMPLE_CONFIG);

    underTest = new FeatureSetRegistry(
        SOURCE, objectMapper, resourceLoader, agentsRegistry);
  }

  @Test
  void shouldReturnCollectionOfAgentConfigSets() {
    when(agentsRegistry.getSingleAgent(NAME_AGENT_CONFIG_NAME)).thenReturn(of(NAME_AGENT));

    underTest.init();
    FeatureSetDto currentFeatureSetDto = underTest.getCurrentAgentConfigSet();

    assertThat(currentFeatureSetDto.getName()).isEqualTo(DEFAULT_FEATURE_SET);
    assertThat(currentFeatureSetDto.getAgentConfigs()).containsExactlyInAnyOrder(
        new FeatureDto(AGENT_FEATURE_NAME, NAME_AGENT.getName(), NAME_AGENT.getSolutions())
    );
  }

  @Test
  void shouldThrowExceptionIfReferencedResourceIsNotPresent() {
    underTest.init();

    assertThatThrownBy(underTest::getCurrentAgentConfigSet)
        .isInstanceOf(NonResolvableFeatureSetException.class);
  }
}
