package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureName;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetFeaturesUseCase {

  private final AgentConfigFeatureDataAccess dataAccess;

  @Transactional(readOnly = true)
  List<AgentConfigFeatureDto> getFeatures(List<AgentConfigFeatureName> names) {
    return dataAccess.getFeatures(names);
  }
}
