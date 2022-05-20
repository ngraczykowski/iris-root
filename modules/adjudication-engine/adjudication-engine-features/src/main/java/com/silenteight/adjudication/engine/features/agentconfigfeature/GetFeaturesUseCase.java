package com.silenteight.adjudication.engine.features.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.features.agentconfigfeature.dto.AgentConfigFeatureDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class GetFeaturesUseCase {

  private final AgentConfigFeatureDataAccess dataAccess;

  @Transactional(readOnly = true)
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  List<AgentConfigFeatureDto> getFeatures(List<Feature> names) {
    return dataAccess.findAll(names);
  }
}
