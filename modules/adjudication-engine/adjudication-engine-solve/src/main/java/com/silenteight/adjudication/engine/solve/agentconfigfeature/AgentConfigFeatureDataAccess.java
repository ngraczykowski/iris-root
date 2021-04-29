package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureName;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;

import java.util.List;

public interface AgentConfigFeatureDataAccess {

  int addFeatures(List<AgentConfigFeatureName> names);

  List<AgentConfigFeatureDto> getFeatures(List<AgentConfigFeatureName> names);
}
