package com.silenteight.payments.bridge.agents.port;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.TwoLinesNameAgentRequest;
import com.silenteight.payments.bridge.agents.model.TwoLinesNameAgentResponse;

public interface TwoLinesNameUseCase {

  TwoLinesNameAgentResponse invoke(@NonNull TwoLinesNameAgentRequest twoLinesNameAgentRequest);
}
