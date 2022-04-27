package com.silenteight.bridge.core.registration.domain.strategy;

import com.silenteight.bridge.core.registration.domain.model.Batch;

public interface BatchCompletionStrategy extends BatchStrategyNameProvider {

  void completeBatch(Batch batch);
}
