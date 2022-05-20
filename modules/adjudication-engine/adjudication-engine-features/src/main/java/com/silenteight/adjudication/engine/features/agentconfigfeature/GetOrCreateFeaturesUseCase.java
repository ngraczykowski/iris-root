package com.silenteight.adjudication.engine.features.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.features.agentconfigfeature.dto.AgentConfigFeatureDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetOrCreateFeaturesUseCase {

  private final CreateUniqueFeaturesUseCase createUniqueFeaturesUseCase;
  private final GetFeaturesUseCase getFeaturesUseCase;

  @Timed(value = "ae.features.use_cases", extraTags = {
      "package", "agentconfigfeature" }, histogram = true,
      percentiles = { 0.5, 0.95, 0.99 })
  List<AgentConfigFeatureDto> getOrCreateFeatures(List<Feature> features) {
    createUniqueFeaturesUseCase.createUniqueFeatures(features);
    return getFeaturesUseCase.getFeatures(features);
  }
}
