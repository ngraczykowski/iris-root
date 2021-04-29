package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureName;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AgentConfigFeatureFacade {

  private final GetOrCreateFeaturesUseCase getOrCreateFeaturesUseCase;

  public List<AgentConfigFeatureDto> getOrCreateFeatures(List<AgentConfigFeatureName> names) {
    return getOrCreateFeaturesUseCase.getOrCreateFeatures(names);
  }
}
