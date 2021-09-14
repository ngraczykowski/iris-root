package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class ManageAgentExchangesJob {

  private final DeleteEmptyAgentExchangesQuery deleteEmptyQuery;
  private final DeleteReceivedAgentExchangeMatchFeaturesQuery deleteReceivedQuery;
  private final DeleteOutdatedAgentExchangeMatchFeaturesQuery deleteOutdatedQuery;

  @SchedulerLock(lockAtMostFor = "PT30M",
      name = "ManageAgentExchangesJob.manageAgentExchanges")
  @Scheduled(initialDelay = 30 * 1000, fixedDelayString =
      "${ae.analysis.agent-exchange.manage-agent-exchanges-job.delay:300000}")
  void manageAgentExchanges() {
    log.info("Deleting already received agent exchanges...");

    LockAssert.assertLocked();

    deleteOutdatedAgentExchangeMatchFeatures();
    deleteReceivedAgentExchangeMatchFeaturesLoop();
    deleteEmptyAgentExchanges();
  }

  private void deleteOutdatedAgentExchangeMatchFeatures() {
    var deletedCount = deleteOutdatedQuery.execute();
    log.info("Deleted outdated agent exchange match features: count={}", deletedCount);
  }

  private void deleteReceivedAgentExchangeMatchFeaturesLoop() {
    var totalDeleted = 0;

    for (int left = 10; left >= 0; left--) {
      var deletedFeatures = deleteReceivedAgentExchangeMatchFeatures();
      totalDeleted += deletedFeatures;

      if (log.isDebugEnabled()) {
        log.debug("Deleted match features: count={}", deletedFeatures);
      }

      if (deletedFeatures == 0) {
        break;
      }

      log.info(
          "Still deleting already received agent exchange match features: loopsLeft={}"
              + ", totalDeleted={}", left, totalDeleted);
    }
  }

  @Timed(value = "ae.analysis.jobs", extraTags = { "package", "agentexchange" })
  private int deleteReceivedAgentExchangeMatchFeatures() {
    return deleteReceivedQuery.execute();
  }

  @Timed(value = "ae.analysis.jobs", extraTags = { "package", "agentexchange" })
  private void deleteEmptyAgentExchanges() {
    var deletedCount = deleteEmptyQuery.execute();
    log.info("Deleted empty agent exchanges: count={}", deletedCount);
  }
}
