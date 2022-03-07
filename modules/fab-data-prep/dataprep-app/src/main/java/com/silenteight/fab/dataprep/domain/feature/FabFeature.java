package com.silenteight.fab.dataprep.domain.feature;

import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;

import java.util.List;

public interface FabFeature<T extends Feature> {

  BatchCreateAgentInputsIn<T> createFeatureInput(FeatureInputsCommand featureInputsCommand);

  default BatchCreateAgentInputsIn<T> toAgentInputs(List<AgentInputIn<T>> agentInputs) {
      return BatchCreateAgentInputsIn.<T>builder()
          .agentInputs(agentInputs)
          .build();
  }
}
