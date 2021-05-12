package com.silenteight.adjudication.engine.features.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class CreateUniqueFeaturesUseCase {

  private final AgentConfigFeatureDataAccess dataAccess;

  @Transactional
  int createUniqueFeatures(List<Feature> features) {
    return dataAccess.addUnique(features);
  }
}
