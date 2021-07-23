package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.FeatureMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.MatchMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@Slf4j
class RecommendationMetadataCollector {

  Collection<AlertMetadata> collectFromRecommendationMetadata(RecommendationMetadata metadata) {
    var matchesMetadata = metadata.getMatchesMetadata();

    if (isNull(matchesMetadata) || matchesMetadata.size() != 1) {
      log.error("Incorrect number of matches metadata");
      return emptyList();
    }

    return new MatchMetadataCollector(matchesMetadata.get(0)).collect();
  }

  @RequiredArgsConstructor
  static class MatchMetadataCollector {

    private final MatchMetadata matchMetadata;
    private final List<AlertMetadata> metadataCollection = new ArrayList<>();

    Collection<AlertMetadata> collect() {
      addAll(matchMetadata.getCategories());
      addAll(matchMetadata.getReason());
      addFeatures(matchMetadata.getFeatures());

      return metadataCollection;
    }

    private void addFeatures(Map<String, FeatureMetadata> features) {
      features.entrySet().stream()
          .forEach(feature -> addFeature(feature.getKey(), feature.getValue()));
    }

    private void addFeature(String featureKey, FeatureMetadata featureMetadata) {
      add(featureKey + ":config", featureMetadata.getAgentConfig());
      add(featureKey + ":solution", featureMetadata.getSolution());
    }

    private void addAll(Map<String, String> values) {
      values.entrySet().stream()
          .forEach(v -> add(v.getKey(), v.getValue()));
    }

    private void add(String key, String value) {
      var metadata = new AlertMetadata(key, value);
      metadataCollection.add(metadata);
    }
  }
}
