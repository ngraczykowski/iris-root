package com.silenteight.bridge.core.registration.domain.strategy;

import com.silenteight.bridge.core.registration.domain.command.DataRetentionStrategyCommand;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionType;

import java.util.Set;

public interface DataRetentionStrategy {

  Set<DataRetentionType> getSupportedDataRetentionTypes();

  void run(DataRetentionStrategyCommand command);
}
