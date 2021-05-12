package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.features.agentconfigfeature.dto.AgentConfigFeatureDto;

import java.util.List;

public interface FeatureProvider {

  List<AgentConfigFeatureDto> getFeatures(List<Feature> features);
}
