package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
class BulkProcessorScheduler {

  private final BulkProcessor bulkProcessor;
  private final BulkRepository bulkRepository;
  private final BulkUpdater bulkUpdater;

  @Scheduled(fixedDelay = 2 * 1000, initialDelay = 2000)
  @SchedulerLock(name = "processPreProcessedBulks", lockAtLeastFor = "PT1S", lockAtMostFor = "PT8S")
  public void processPreProcessedBulks() {
    LockAssert.assertLocked();

    log.trace("Scheduler has been triggered...");

    bulkRepository
        .findFirstByStatusOrderByCreatedAtAsc(BulkStatus.PRE_PROCESSED)
        .ifPresent(bulk -> {
          log.info(
              "Try to pre process bulk from status PRE_PROCESSED to PRE_PROCESSING with id: {}",
              bulk.getId());
          bulkUpdater.updateWithPreProcessingStatus(bulk.getId());
        });

    bulkRepository
        .findFirstByStatusOrderByCreatedAtAsc(BulkStatus.PRE_PROCESSING)
        .ifPresent(bulk -> {
          log.info(
              "Processing bulk with id: {}, learning: {}", bulk.getId(), bulk.isLearning());
          if (bulk.isLearning()) {
            bulkProcessor.tryToProcessLearningBulk(bulk.getId());
          } else {
            bulkProcessor.tryToProcessSolvingBulk(bulk.getId());
          }
        });
  }
}
