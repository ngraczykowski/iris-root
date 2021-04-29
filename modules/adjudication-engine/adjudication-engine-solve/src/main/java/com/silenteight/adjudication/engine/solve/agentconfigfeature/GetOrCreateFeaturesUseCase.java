package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureName;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetOrCreateFeaturesUseCase {

  private final CreateUniqueFeaturesUseCase createUniqueFeaturesUseCase;
  private final GetFeaturesUseCase getFeaturesUseCase;

  List<AgentConfigFeatureDto> getOrCreateFeatures(List<AgentConfigFeatureName> names) {
    createUniqueFeaturesUseCase.createUniqueFeatures(names);
    return getFeaturesUseCase.getFeatures(names);
  }
}
