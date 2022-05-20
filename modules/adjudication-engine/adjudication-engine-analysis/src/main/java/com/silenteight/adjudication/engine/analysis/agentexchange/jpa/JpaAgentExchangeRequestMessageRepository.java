package com.silenteight.adjudication.engine.analysis.agentexchange.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeRequestMessageRepository;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.persistence.EntityManager;

// TODO(ahaczewski): Test JpaAgentExchangeRequestMessageRepository class (integration test with DB).
@RequiredArgsConstructor
@Repository
@Slf4j
class JpaAgentExchangeRequestMessageRepository implements AgentExchangeRequestMessageRepository {

  private final EntityManager entityManager;

  @Transactional
  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public void save(AgentExchangeRequestMessage message) {
    if (log.isTraceEnabled()) {
      log.trace("Saving agent exchange request: message={}", message);
    }

    var agentExchange = new AgentExchange(message);

    entityManager.persist(agentExchange);

    message.forEachFeature(feature ->
        entityManager.persist(new AgentExchangeFeature(agentExchange, feature)));

    message.forEachMatch((matchId, agentConfigFeatureIds) ->
        agentConfigFeatureIds.forEach(agentConfigFeatureId ->
            entityManager.persist(
                new AgentExchangeMatchFeature(agentExchange, agentConfigFeatureId, matchId))));
  }

  @Transactional
  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public void saveAll(List<AgentExchangeRequestMessage> messages) {
    log.debug("Saving agent requests: messageCount={}", messages.size());
    messages.forEach(this::save);
  }
}
