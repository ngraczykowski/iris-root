package com.silenteight.serp.governance.model.feature;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.serp.governance.agent.domain.FeaturesProvider;
import com.silenteight.serp.governance.agent.domain.dto.FeatureDto;
import com.silenteight.serp.governance.model.provide.PolicyFeatureProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class FeatureSolver implements PolicyFeatureProvider {

  @NonNull
  private final FeaturesProvider featuresProvider;

  @Override
  public List<Feature> resolveFeatures(List<String> features) {
    return featuresProvider
        .getFeaturesListDto()
        .getFeatures()
        .stream()
        .filter(feature -> features.contains(feature.getName()))
        .map(FeatureSolver::toFeature)
        .collect(toList());
  }

  private static Feature toFeature(FeatureDto featureDto) {
    return Feature
        .newBuilder()
        .setName(featureDto.getName())
        .setAgentConfig(featureDto.getAgentConfig())
        .build();
  }
}
