package com.silenteight.adjudication.engine.features.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.features.agentconfigfeature.dto.AgentConfigFeatureDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AgentConfigFeatureFacade {

  private final GetOrCreateFeaturesUseCase getOrCreateFeaturesUseCase;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<AgentConfigFeatureDto> getOrCreateFeatures(List<Feature> features) {
    return getOrCreateFeaturesUseCase.getOrCreateFeatures(features);
  }
}
