package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class CreateUniqueFeaturesUseCase {

  private final AgentConfigFeatureDataAccess dataAccess;

  @Transactional
  int createUniqueFeatures(List<AgentConfigFeatureName> names) {
    return dataAccess.addFeatures(names);
  }
}
