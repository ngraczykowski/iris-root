package com.silenteight.adjudication.engine.analysis.analysis.features;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.analysis.analysis.FeatureProvider;
import com.silenteight.adjudication.engine.features.agentconfigfeature.AgentConfigFeatureFacade;
import com.silenteight.adjudication.engine.features.agentconfigfeature.dto.AgentConfigFeatureDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class SolveFeatureProvider implements FeatureProvider {

  private final AgentConfigFeatureFacade facade;

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public List<AgentConfigFeatureDto> getFeatures(List<Feature> features) {
    return facade.getOrCreateFeatures(features);
  }
}
