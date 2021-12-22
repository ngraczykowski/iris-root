package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchFeatureRequestFactoryTest {

  private static final String AGENT_INPUT_TYPE = "AgentInputType";

  @Mock
  private FeaturesProperties featuresProperties;

  private BatchFeatureRequestFactory factory;

  @BeforeEach
  void beforeEach() {
    factory = new BatchFeatureRequestFactory(featuresProperties);
  }

  @Test
  public void shouldReturnValidFeatures() {
    when(featuresProperties.getMapping()).thenReturn(new HashMap<>());

    List<String> matches = List.of("alerts/1/matches/1", "alerts/1/matches/2");
    List<String> features = List.of("features/geo", "features/name");

    var batchFeatureRequest = factory.create(AGENT_INPUT_TYPE, matches, features);

    assertThat(batchFeatureRequest.getFeatures().size()).isEqualTo(2);
    assertThat(batchFeatureRequest.getMatches().size()).isEqualTo(2);

    assertThat(batchFeatureRequest.getFeatures().stream()
        .filter(f -> f.equals("features/geo"))
        .count()).isEqualTo(1);

    assertThat(batchFeatureRequest.getFeatures().stream()
        .filter(f -> f.equals("features/name"))
        .count()).isEqualTo(1);
  }

  @Test
  public void shouldReturnMappedGeoFeature() {
    when(featuresProperties.getMapping()).thenReturn(Map.of("geo2", "geo"));

    List<String> matches = List.of("alerts/1/matches/1", "alerts/1/matches/2");
    List<String> features = List.of("features/geo2", "features/name");

    var batchFeatureRequest = factory.create(AGENT_INPUT_TYPE, matches, features);

    assertThat(batchFeatureRequest.getFeatures().size()).isEqualTo(2);
    assertThat(batchFeatureRequest.getMatches().size()).isEqualTo(2);

    assertThat(batchFeatureRequest.getFeatures().stream()
        .filter(f -> f.equals("features/geo"))
        .count()).isEqualTo(1);

    assertThat(batchFeatureRequest.getFeatures().stream()
        .filter(f -> f.equals("features/name"))
        .count()).isEqualTo(1);
  }
}
