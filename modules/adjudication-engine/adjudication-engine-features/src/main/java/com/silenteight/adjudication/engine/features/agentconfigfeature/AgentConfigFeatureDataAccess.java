package com.silenteight.adjudication.engine.features.agentconfigfeature;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.features.agentconfigfeature.dto.AgentConfigFeatureDto;

import java.util.List;

public interface AgentConfigFeatureDataAccess {

  int addUnique(List<Feature> features);

  List<AgentConfigFeatureDto> findAll(List<Feature> features);
}
