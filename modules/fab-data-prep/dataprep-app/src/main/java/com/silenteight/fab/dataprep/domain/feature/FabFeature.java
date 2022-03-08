package com.silenteight.fab.dataprep.domain.feature;

import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;

import java.util.List;

public interface FabFeature {

  List<AgentInputIn<Feature>> createFeatureInput(FeatureInputsCommand featureInputsCommand);
}
