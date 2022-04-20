package com.silenteight.bridge.core.registration.domain.strategy;

import com.silenteight.bridge.core.registration.domain.command.RegisterBatchCommand;
import com.silenteight.bridge.core.registration.domain.model.Batch;

public interface BatchRegistrationStrategy extends BatchStrategyNameProvider {

  Batch register(RegisterBatchCommand registerBatchCommand);
}
