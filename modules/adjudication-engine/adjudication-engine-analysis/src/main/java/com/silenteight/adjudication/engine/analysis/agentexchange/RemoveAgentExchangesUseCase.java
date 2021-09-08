package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
class RemoveAgentExchangesUseCase {

  private final AgentExchangeDataAccess agentExchangeDataAccess;

  @Timed(value = "ae.analysis.use_cases", extraTags = { "package", "agentexchange" })
  @Transactional
  void removeAgentExchanges() {
    if (log.isDebugEnabled()) {
      log.debug(
          "Removing agent exchanges");
    }

    agentExchangeDataAccess.removeAgentExchange();
  }
}
