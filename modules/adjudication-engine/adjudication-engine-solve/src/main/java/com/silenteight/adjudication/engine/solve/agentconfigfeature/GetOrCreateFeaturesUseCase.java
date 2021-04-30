package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetOrCreateFeaturesUseCase {

  private final CreateUniqueFeaturesUseCase createUniqueFeaturesUseCase;
  private final GetFeaturesUseCase getFeaturesUseCase;

  List<AgentConfigFeatureDto> getOrCreateFeatures(List<Feature> features) {
    createUniqueFeaturesUseCase.createUniqueFeatures(features);
    return getFeaturesUseCase.getFeatures(features);
  }
}
