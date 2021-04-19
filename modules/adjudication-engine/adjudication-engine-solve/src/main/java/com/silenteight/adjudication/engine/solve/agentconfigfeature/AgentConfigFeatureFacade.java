package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AgentConfigFeatureFacade {

  @NonNull
  private final GetOrCreateFeaturesUseCase getOrCreateFeaturesUseCase;


}
