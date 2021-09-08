package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.PRE_PROCESSED;

@RequiredArgsConstructor
@Slf4j
public class BulkProcessorScheduler {

  private final BulkProcessor bulkProcessor;
  private final BulkRepository bulkRepository;
  private final BulkUpdater bulkUpdater;

  @Scheduled(fixedDelay = 2 * 1000, initialDelay = 2000)
  @SchedulerLock(name = "processPreProcessedBulks", lockAtLeastFor = "PT1S", lockAtMostFor = "PT2M")
  public void processPreProcessedBulks() {
    LockAssert.assertLocked();

    log.trace("Scheduler has been triggered...");

    bulkRepository.findFirstByStatusOrderByCreatedAtAsc(PRE_PROCESSED).ifPresent(b -> {
      log.info(
          "Try to pre process bulk from status PRE_PROCESSED to PRE_PROCESSING with id: {}",
          b.getId());
      bulkUpdater.updateWithPreProcessingStatus(b.getId());
      bulkProcessor.tryToProcessBulk();
    });
  }
}
