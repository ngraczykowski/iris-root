package com.silenteight.adjudication.engine.analysis.agentexchange;

import org.springframework.data.repository.Repository;

interface AgentExchangeMatchFeaturesRepository
    extends Repository<AgentExchangeMatchFeature, AgentExchangeMatchFeatureKey> {
  //
  //  @Nonnull
  //  AgentExchangeMatchFeature save(
  //      AgentExchangeMatchFeature agentExchangeMatchFeature);
  //
  //  AgentExchange findByFeatureAndAgentConfigAndPriority(
  //      String feature, String agentConfig, String priority);
  //
  //  void deleteAllByCreatedAtBefore(OffsetDateTime before);
  //
  //  long countAllByCreatedAtBefore(OffsetDateTime before);
}
