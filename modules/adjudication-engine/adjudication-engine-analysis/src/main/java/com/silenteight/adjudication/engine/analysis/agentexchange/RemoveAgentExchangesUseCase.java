package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.DeleteAgentExchangeRequest;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class RemoveAgentExchangesUseCase {

  private final AgentExchangeDataAccess agentExchangeDataAccess;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "agentexchange" })
  @Transactional
  void removeAgentExchanges(List<DeleteAgentExchangeRequest> deleteAgentExchangeRequests) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Removing agent exchanges: deleteRequestCount={}", deleteAgentExchangeRequests.size());
    }

    deleteAgentExchangeRequests.forEach(
        request -> agentExchangeDataAccess.removeAgentExchange(
            request.getAgentExchangeRequestId(),
            request.getMatchId(),
            request.getFeaturesIds()));
  }
}
