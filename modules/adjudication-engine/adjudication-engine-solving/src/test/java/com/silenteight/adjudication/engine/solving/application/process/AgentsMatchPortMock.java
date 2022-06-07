/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.port.AgentsMatchPort;
import com.silenteight.adjudication.engine.solving.domain.AgentExchangeRequestMessage;

import java.util.ArrayList;
import java.util.List;

class AgentsMatchPortMock implements AgentsMatchPort {

  private List<AgentExchangeRequestMessage> requests = new ArrayList<>();

  @Override
  public void publish(AgentExchangeRequestMessage message) {
    requests.add(message);
  }
}
