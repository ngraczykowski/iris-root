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

    var firstByStatusOrderByCreatedAtAsc =
        bulkRepository.findFirstByStatusOrderByCreatedAtAsc(PRE_PROCESSED);

    if (firstByStatusOrderByCreatedAtAsc.isPresent()) {
      log.info("Try to process bulk with id: {}", firstByStatusOrderByCreatedAtAsc.get().getId());
    } else {
      log.info("No bulk with status PRE_PROCESSED");
    }

    firstByStatusOrderByCreatedAtAsc.ifPresent(b -> {
      bulkUpdater.updateWithPreProcessingStatus(b.getId());
      bulkProcessor.tryToProcessBulk();
    });
  }
}
