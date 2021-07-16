package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.FeatureMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.MatchMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;
import com.silenteight.hsbc.datasource.feature.Feature;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@Slf4j
class AlertMetadataCollector {

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

    private static final String FV_SIGNATURE = "feature_vector_signature";
    private static final String POLICY = "policy";
    private static final String STEP = "step";

    Collection<AlertMetadata> collect() {
      addMatchReason(matchMetadata.getReason());
      addFeatures(matchMetadata.getFeatures());
      addCategories(matchMetadata.getCategories());

      return metadataCollection;
    }

    private void addFeatures(Map<String, FeatureMetadata> features) {
      stream(Feature.values())
          .forEach(f -> addFeature(f, features));
    }

    private void addFeature(Feature feature, Map<String, FeatureMetadata> features) {
      var featureName = feature.getFullName();
      if (features.containsKey(featureName)) {
        var featureMetadata = features.get(featureName);
        var prefix = feature.getName() + "Feature";

        add(prefix + "Config", featureMetadata.getAgentConfig());
        add(prefix + "Solution", featureMetadata.getSolution());
      }
    }

    private void addCategories(Map<String, String> categories) {
      categories.entrySet().stream()
          .forEach(c -> add(c.getKey(), c.getValue()));
    }

    private void addMatchReason(Map<String, String> reason) {
      add(FV_SIGNATURE, reason.getOrDefault(FV_SIGNATURE, ""));
      add(POLICY, reason.getOrDefault(POLICY, ""));
      add(STEP, reason.getOrDefault(STEP, ""));
    }

    private void add(String key, String value) {
      var metadata = new AlertMetadata();
      metadata.setKey(key);
      metadata.setValue(value);
      metadataCollection.add(metadata);
    }
  }
}
