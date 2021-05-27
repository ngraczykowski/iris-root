package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.UUID;
import java.util.stream.Stream;

class InMemoryAgentExchangeFeatureQueryRepository implements AgentExchangeFeatureQueryRepository {

  private final Multimap<UUID, AgentExchangeFeatureQuery> store = ArrayListMultimap.create();

  @Override
  public Stream<AgentExchangeFeatureQuery> findAllByRequestId(UUID requestId) {
    return store.get(requestId).stream();
  }

  @Override
  public boolean agentExchangeRequestExists(UUID requestId) {
    return store.containsKey(requestId);
  }

  AgentExchangeFeatureQuery save(AgentExchangeFeatureQuery entity) {
    store.put(entity.getRequestId(), entity);
    return entity;
  }
}
