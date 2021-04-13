package com.silenteight.adjudication.engine.solve;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import javax.annotation.Nonnull;

interface AgentExchangeMatchFeaturesRepository
    extends Repository<AgentExchangeMatchFeaturesEntity, AgentExchangeMatchFeaturesKey> {

  @Nonnull
  AgentExchangeMatchFeaturesEntity save(
      AgentExchangeMatchFeaturesEntity agentExchangeMatchFeaturesEntity);

  AgentExchangeEntity findByFeatureAndAgentConfigAndPriority(
      String feature, String agentConfig, String priority);

  void deleteAllByCreatedAtBefore(OffsetDateTime before);

  long countAllByCreatedAtBefore(OffsetDateTime before);
}
