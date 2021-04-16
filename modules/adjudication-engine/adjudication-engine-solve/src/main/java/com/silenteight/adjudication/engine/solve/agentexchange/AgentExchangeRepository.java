package com.silenteight.adjudication.engine.solve.agentexchange;

import org.springframework.data.repository.Repository;

import java.util.UUID;
import javax.annotation.Nonnull;

interface AgentExchangeRepository extends Repository<AgentExchange, UUID> {

  @Nonnull
  AgentExchange save(AgentExchange agentExchange);

//  AgentExchange findByFeatureAndAgentConfigAndPriority(
//      String feature, String agentConfig, String priority);
}
