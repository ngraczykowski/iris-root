package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AgentConfigFeatureFacade {

  private final GetOrCreateFeaturesUseCase getOrCreateFeaturesUseCase;

  public List<AgentConfigFeatureDto> getOrCreateFeatures(List<Feature> features) {
    return getOrCreateFeaturesUseCase.getOrCreateFeatures(features);
  }
}
