package com.silenteight.payments.bridge.datasource.feature.port.incoming;

import java.util.Collection;
import java.util.function.Consumer;

public interface BatchGetFeatureInputUseCase {

  void batchGetFeatureInput(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<BatchFeatureInputResponse> consumer);
}
