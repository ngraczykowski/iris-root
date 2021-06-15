package com.silenteight.serp.governance.model.feature;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.serp.governance.agent.domain.FeaturesProvider;
import com.silenteight.serp.governance.agent.domain.dto.FeatureDto;
import com.silenteight.serp.governance.agent.domain.dto.FeaturesListDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureSolverTest {

  private static final String FEATURE_NAME_NAME = "features/name";
  private static final String FEATURE_NAME_DOB = "features/dob";
  private static final String NAME_AGENT_CONFIG = "agents/name/versions/1.0.0/configs/1";
  private static final String DATE_AGENT_CONFIG = "agents/date/versions/1.0.0/configs/1";
  private static final List<String> SOLUTIONS = asList("MATCH", "NO_MATCH");

  private static final FeatureDto NAME_FEATURE = FeatureDto.builder()
      .name(FEATURE_NAME_NAME)
      .agentConfig(NAME_AGENT_CONFIG)
      .solutions(SOLUTIONS)
      .build();
  private static final FeatureDto DATE_FEATURE = FeatureDto.builder()
      .name(FEATURE_NAME_DOB)
      .agentConfig(DATE_AGENT_CONFIG)
      .solutions(SOLUTIONS)
      .build();

  private static final FeaturesListDto FEATURES_LIST = new FeaturesListDto(
      asList(NAME_FEATURE, DATE_FEATURE));

  @Mock
  FeaturesProvider featuresProvider;

  FeatureSolver underTest;

  @BeforeEach
  void init() {
    underTest = new FeatureSolver(featuresProvider);
  }

  @Test
  void shouldResolveGivenFeatures() {
    when(featuresProvider.getFeaturesListDto()).thenReturn(FEATURES_LIST);

    List<Feature> features = underTest.resolveFeatures(List.of(FEATURE_NAME_NAME));

    assertThat(features).containsExactly(getFeature(FEATURE_NAME_NAME, NAME_AGENT_CONFIG));
  }

  private Feature getFeature(String featureName, String agentName) {
    return Feature
        .newBuilder()
        .setName(featureName)
        .setAgentConfig(agentName)
        .build();
  }
}
