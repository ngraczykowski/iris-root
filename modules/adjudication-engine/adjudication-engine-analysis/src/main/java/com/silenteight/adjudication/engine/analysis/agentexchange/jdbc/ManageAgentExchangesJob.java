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
      name = "ManageAgentExchangesJob.deleteReceivedAgentExchangesLoop")
  @Scheduled(initialDelay = 30 * 60 * 1000, fixedDelayString =
      "${ae.analysis.agent-exchange.delete-received-job.delay:300000}")
  void deleteReceivedAgentExchangesLoop() {
    log.info("Deleting already received agent exchange features...");

    LockAssert.assertLocked();

    deleteReceivedAgentExchangeMatchFeaturesLoop();
    deleteEmptyAgentExchanges();
  }

  private void deleteReceivedAgentExchangeMatchFeaturesLoop() {
    var totalDeleted = 0;

    for (int left = 10; left >= 0; left--) {
      var deletedFeatures = deleteReceivedAgentExchangeMatchFeatures();
      totalDeleted += deletedFeatures;

      if (log.isDebugEnabled()) {
        log.debug("Deleted features: count={}", deletedFeatures);
      }

      if (deletedFeatures == 0) {
        break;
      }

      log.info(
          "Still deleting already received agent exchange features: loopsLeft={}, totalDeleted={}",
          left, totalDeleted);
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

  @SchedulerLock(lockAtMostFor = "PT30M",
      name = "ManageAgentExchangesJob.deleteOutdatedAgentExchanges")
  @Scheduled(initialDelay = 180 * 60 * 1000, fixedDelayString =
      "${ae.analysis.agent-exchange.delete-outdated-job.delay:300000}")
  void deleteOutdatedAgentExchanges() {
    log.info("Deleting already received agent exchange features...");

    LockAssert.assertLocked();

    deleteOutdatedAgentExchangeMatchFeatures();
    deleteEmptyAgentExchanges();
  }

  private void deleteOutdatedAgentExchangeMatchFeatures() {
    var deletedCount = deleteOutdatedQuery.execute();
    log.info("Deleted outdated agent exchange match features: count={}", deletedCount);
  }
}
