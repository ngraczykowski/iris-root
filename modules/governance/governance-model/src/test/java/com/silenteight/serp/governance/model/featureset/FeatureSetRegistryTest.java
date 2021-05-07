package com.silenteight.serp.governance.model.featureset;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.serp.governance.agent.AgentDto;
import com.silenteight.serp.governance.agent.AgentsRegistry;
import com.silenteight.serp.governance.model.TestResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.serp.governance.agent.AgentFixture.DATE_AGENT;
import static com.silenteight.serp.governance.agent.AgentFixture.NAME_AGENT;
import static com.silenteight.serp.governance.agent.config.AgentConfigFixture.DATE_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.config.AgentConfigFixture.NAME_AGENT_CONFIG_NAME;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.AGENT_FEATURE_DATE;
import static com.silenteight.serp.governance.agent.details.AgentDetailsFixture.AGENT_FEATURE_NAME;
import static com.silenteight.serp.governance.model.featureset.FeatureSetRegistry.DEFAULT_FEATURE_SET;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureSetRegistryTest {

  private static final String FEATURE_NAME_NAME = "features/name";
  private static final String FEATURE_NAME_DOB = "features/dob";

  private static final String SAMPLE_CONFIG = "["
      + "  {"
      + "    \"name\" : \"" + DEFAULT_FEATURE_SET + "\","
      + "    \"features\": [{"
      + "      \"name\": \"" + AGENT_FEATURE_NAME + "\","
      + "      \"agentConfig\": \"" + NAME_AGENT_CONFIG_NAME + "\""
      + "    },{"
      + "      \"name\": \"" + AGENT_FEATURE_DATE + "\","
      + "      \"agentConfig\": \"" + DATE_AGENT_CONFIG_NAME + "\""
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
  void shouldReturnValidFeatureSet() {
    when(agentsRegistry.getSingleAgent(NAME_AGENT_CONFIG_NAME)).thenReturn(of(NAME_AGENT));
    when(agentsRegistry.getSingleAgent(DATE_AGENT_CONFIG_NAME)).thenReturn(of(DATE_AGENT));

    underTest.init();
    FeatureSetDto currentFeatureSetDto = underTest.getCurrentFeatureSet();

    assertThat(currentFeatureSetDto.getName()).isEqualTo(DEFAULT_FEATURE_SET);
    assertThat(currentFeatureSetDto.getFeatures()).containsExactlyInAnyOrder(
        new FeatureDto(AGENT_FEATURE_NAME, NAME_AGENT.getName(), NAME_AGENT.getSolutions()),
        new FeatureDto(AGENT_FEATURE_DATE, DATE_AGENT.getName(), DATE_AGENT.getSolutions())
    );
  }

  @Test
  void shouldThrowExceptionIfReferencedResourceIsNotPresent() {
    underTest.init();

    assertThatThrownBy(underTest::getCurrentFeatureSet)
        .isInstanceOf(NonResolvableFeatureSetException.class);
  }

  @Test
  void shouldEnsureExposedFeatureSetIsValid() {
    AgentDto misconfiguredAgent = AgentDto.builder()
        .name(NAME_AGENT_CONFIG_NAME)
        .features(List.of(AGENT_FEATURE_DATE))
        .solutions(List.of())
        .build();

    when(agentsRegistry.getSingleAgent(NAME_AGENT_CONFIG_NAME)).thenReturn(of(misconfiguredAgent));

    underTest.init();
    assertThatThrownBy(() -> underTest.getFeatureSet(DEFAULT_FEATURE_SET))
        .isInstanceOf(InvalidFeatureSetException.class)
        .hasMessageContaining("is not capable of solving feature '" + AGENT_FEATURE_NAME + "'");
  }

  @Test
  void shouldResolveGivenFeatures() {
    //given
    when(agentsRegistry.getSingleAgent(NAME_AGENT_CONFIG_NAME)).thenReturn(of(NAME_AGENT));
    when(agentsRegistry.getSingleAgent(DATE_AGENT_CONFIG_NAME)).thenReturn(of(DATE_AGENT));
    underTest.init();
    //when
    List<Feature> features = underTest.resolveFeatures(List.of(AGENT_FEATURE_NAME));
    //then
    assertThat(features).containsExactlyInAnyOrder(
        getFeature(AGENT_FEATURE_NAME, NAME_AGENT_CONFIG_NAME));
  }

  private Feature getFeature(String featureName, String agentName) {
    return Feature
        .newBuilder()
        .setName(featureName)
        .setAgentConfig(agentName)
        .build();
  }
}
