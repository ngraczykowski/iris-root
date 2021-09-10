package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class DeleteFinishedAgentExchangesJob {

  private final DeleteFinishedAgentExchangesQuery query;

  @SchedulerLock(lockAtLeastFor = "PT20M", name = "DeleteFinishedAgentExchangesJob")
  @Scheduled(initialDelayString = "300000", fixedDelayString =
      "${ae.analysis.agent-exchange.delete-finished-agent-exchanges-job.delay:300000}")
  void deleteFinishedAgentExchangesLoop() {
    log.info("Deleting already finished agent exchanges...");

    for (int left = 10; left >= 0; left--) {
      var deletedCount = deleteFinishedAgentExchanges();

      if (log.isDebugEnabled()) {
        log.debug("Deleted exchanges: count={}", deletedCount);
      }

      if (deletedCount == 0) {
        break;
      }

      log.info("Still deleting already finished agent exchanges: left={}", left);
    }
  }

  @Timed(value = "ae.analysis.jobs", extraTags = { "package", "agentexchange" })
  private int deleteFinishedAgentExchanges() {
    return query.execute();
  }
}
