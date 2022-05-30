package com.silenteight.bridge.core.registration.domain.strategy;

import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionMode;

public interface DataRetentionStrategy {

  DataRetentionMode getSupportedDataRetentionMode();

  void run(DataRetentionStrategyCommand command);
}
