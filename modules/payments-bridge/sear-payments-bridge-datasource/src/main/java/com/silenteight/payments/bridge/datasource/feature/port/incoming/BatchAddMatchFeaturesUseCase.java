package com.silenteight.payments.bridge.datasource.feature.port.incoming;

import java.util.Collection;

public interface BatchAddMatchFeaturesUseCase {

  void batchAddMatchFeatures(Collection<AddMatchFeaturesRequest> requests);
}
