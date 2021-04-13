package com.silenteight.adjudication.engine.solve;

import org.springframework.data.repository.Repository;

import java.util.UUID;
import javax.annotation.Nonnull;

interface AgentExchangeRepository extends Repository<AgentExchangeEntity, UUID> {

  @Nonnull
  AgentExchangeEntity save(AgentExchangeEntity agentExchangeEntity);

  AgentExchangeEntity findByFeatureAndAgentConfigAndPriority(
      String feature, String agentConfig, String priority);
}
