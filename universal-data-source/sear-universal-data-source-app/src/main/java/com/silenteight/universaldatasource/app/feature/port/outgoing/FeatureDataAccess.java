package com.silenteight.universaldatasource.app.feature.port.outgoing;

import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface FeatureDataAccess {

  List<CreatedAgentInput> saveAll(List<MatchFeatureInput> matchFeatureInputs);

  int stream(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<MatchFeatureOutput> consumer);
}
