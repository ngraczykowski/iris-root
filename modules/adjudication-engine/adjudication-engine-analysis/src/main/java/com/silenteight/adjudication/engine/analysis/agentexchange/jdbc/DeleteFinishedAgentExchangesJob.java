package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class DeleteFinishedAgentExchangesJob {

  private final DeleteFinishedAgentExchangesQuery query;

  @Scheduled(initialDelayString = "1m", fixedDelayString = "2m")
  void deleteFinishedAgentExchanges() {
    for (int i = 10; i >= 0; i--) {
      if (query.execute() == 0)
        break;
    }
  }
}
