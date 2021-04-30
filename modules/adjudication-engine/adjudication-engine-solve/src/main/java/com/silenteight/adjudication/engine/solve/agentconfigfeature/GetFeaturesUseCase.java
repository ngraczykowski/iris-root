package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetFeaturesUseCase {

  private final AgentConfigFeatureDataAccess dataAccess;

  @Transactional(readOnly = true)
  List<AgentConfigFeatureDto> getFeatures(List<Feature> names) {
    return dataAccess.findAll(names);
  }
}
