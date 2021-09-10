package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class DeleteFinishedAgentExchangesJob {

  private final DeleteFinishedAgentExchangesQuery query;

  @Timed(value = "ae.analysis.jobs", extraTags = { "package", "agentexchange" })
  @Scheduled(initialDelayString = "1m", fixedDelayString =
      "${ae.analysis.agent-exchange.delete-finished-agent-exchanges-job.delay:2m}")
  void deleteFinishedAgentExchanges() {
    log.info("Deleting already finished agent exchanges...");

    for (int left = 10; left >= 0; left--) {
      var deletedCount = query.execute();

      if (log.isDebugEnabled()) {
        log.debug("Deleted exchanges: count={}", deletedCount);
      }

      if (deletedCount == 0) {
        break;
      }

      log.info("Still deleting already finished agent exchanges: left={}", left);
    }
  }
}
