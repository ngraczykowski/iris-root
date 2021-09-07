package com.silenteight.payments.bridge.datasource.feature.port.outgoing;

import com.silenteight.payments.bridge.datasource.feature.model.MatchFeatureInput;
import com.silenteight.payments.bridge.datasource.feature.model.MatchFeatureOutput;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface FeatureDataAccess {

  void saveAll(List<MatchFeatureInput> matchFeatureInputs);

  int stream(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<MatchFeatureOutput> consumer);
}
