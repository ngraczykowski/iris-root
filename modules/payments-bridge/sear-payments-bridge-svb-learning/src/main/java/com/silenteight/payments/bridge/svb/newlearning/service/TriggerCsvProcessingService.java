package com.silenteight.payments.bridge.svb.newlearning.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.port.FileListPort;
import com.silenteight.payments.bridge.svb.newlearning.port.LearningDataAccess;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class TriggerCsvProcessingService {

  private final FileListPort fileListPort;
  private final LearningDataAccess dataAccess;

  @Scheduled(cron = "${pb.svb-learning.trigger-csv-processing.cron}")
  @SchedulerLock(name = "processLearningScheduler",
      lockAtMostFor = "${pb.svb-learning.trigger-csv-processing.lock-most}",
      lockAtLeastFor = "${pb.svb-learning.trigger-csv-processing.lock-least}")
  public void process() {
    var list = fileListPort.getFilesList();
    var newNames = dataAccess.saveNonProcessedFileNames(list);

    log.debug("Received non processed files = {}", newNames);

    // TODO Trigger batch job processing
  }
}
