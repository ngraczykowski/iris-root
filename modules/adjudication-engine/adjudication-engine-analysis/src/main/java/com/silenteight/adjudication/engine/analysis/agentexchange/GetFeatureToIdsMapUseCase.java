package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
class GetFeatureToIdsMapUseCase {

  private final AgentExchangeFeatureQueryRepository repository;

  @Transactional(readOnly = true)
  Map<String, Long> getFeatureToIdsMap(UUID agentExchangeRequestId) {
    if (!repository.agentExchangeRequestExists(agentExchangeRequestId)) {
      log.warn("Unknown agent exchange request: requestId={}", agentExchangeRequestId);
      return Collections.emptyMap();
    }

    return repository.findAllByRequestId(agentExchangeRequestId)
        .collect(Collectors.toMap(
            AgentExchangeFeatureQuery::getFeature,
            AgentExchangeFeatureQuery::getAgentConfigFeatureId));
  }
}
