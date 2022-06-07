/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process.port;

import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

public interface AgentResponsePort {

  void processMatchesFeatureValue(final AgentExchangeResponse agentResponse);
}
