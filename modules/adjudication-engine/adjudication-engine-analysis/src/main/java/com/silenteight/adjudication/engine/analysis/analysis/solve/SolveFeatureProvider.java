package com.silenteight.adjudication.engine.analysis.analysis.solve;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.analysis.analysis.FeatureProvider;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.AgentConfigFeatureFacade;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;

import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class SolveFeatureProvider implements FeatureProvider {

  private final AgentConfigFeatureFacade facade;

  @Override
  public List<AgentConfigFeatureDto> getFeatures(List<Feature> features) {
    return facade.getOrCreateFeatures(features);
  }
}
