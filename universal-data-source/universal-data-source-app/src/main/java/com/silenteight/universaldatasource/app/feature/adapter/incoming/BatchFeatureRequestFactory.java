package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(FeaturesProperties.class)
class BatchFeatureRequestFactory {

  private static final String FEATURE_NAME_PREFIX = "features/";

  private final FeaturesProperties featuresProperties;

  BatchFeatureRequest create(
      String agentInputType, List<String> matches, List<String> featureNames) {
    return BatchFeatureRequest.builder()
        .agentInputType(agentInputType)
        .matches(matches)
        .features(getMappedFeatureNames(featureNames))
        .build();
  }

  private List<String> getMappedFeatureNames(List<String> features) {
    return features.stream()
        .map(this::getFeature)
        .collect(toList());
  }

  private String getFeature(String featureName) {
    var featureMapping = featuresProperties.getMapping();
    var featureId = getFeatureId(featureName);
    if (featureMapping.containsKey(featureId)) {
      var mappedFeatureId = featureMapping.get(featureId);
      return getFeatureName(mappedFeatureId);
    }
    return featureName;
  }

  private static String getFeatureId(String featureName) {
    return featureName.replace(FEATURE_NAME_PREFIX, "");
  }

  private static String getFeatureName(String featureName) {
    return FEATURE_NAME_PREFIX + featureName;
  }
}
