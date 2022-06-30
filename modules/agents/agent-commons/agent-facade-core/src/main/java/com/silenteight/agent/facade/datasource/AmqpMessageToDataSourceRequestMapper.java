package com.silenteight.agent.facade.datasource;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

public interface AmqpMessageToDataSourceRequestMapper<T> {

  T map(AgentExchangeRequest request);
}
