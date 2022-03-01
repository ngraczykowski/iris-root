package com.silenteight.payments.bridge.app.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.ObjectPath;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;
import com.silenteight.payments.bridge.svb.learning.domain.CsvProcessingFileStatus;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
class LearningCsvFileTrigger {

  private final LearningRunnerService learningRunnerService;
  private final CsvFileResourceProvider csvFileResourceProvider;
  private final LearningFileRepository learningFileRepository;

  @Scheduled(cron = "${pb.sear-learning.trigger-csv-processing.cron}")
  @SchedulerLock(name = "processLearningScheduler",
      lockAtMostFor = "${pb.sear-learning.trigger-csv-processing.lock-most}",
      lockAtLeastFor = "${pb.sear-learning.trigger-csv-processing.lock-least}")
  public void process() {
    var repositoryFiles = csvFileResourceProvider.getFilesList();
    var newNames = getNonProcessedFiles(repositoryFiles);
    var savedNames = saveNonProcessedFiles(newNames);

    if (log.isDebugEnabled()) {
      log.debug("Trigger non processed files count = {}", savedNames.size());
    }

    newNames.forEach(file -> {
      learningRunnerService.trigger(file);
      file.setStatus(CsvProcessingFileStatus.TRIGGERED.name());
      learningFileRepository.save(file);
    });
  }

  List<LearningFileEntity> getNonProcessedFiles(List<ObjectPath> files) {
    return files
        .stream()
        .map(file -> {
          var learningFile =
              learningFileRepository.findAllByFileNameAndBucketName(
                  file.getName(), file.getBucket());

          if (learningFile.isEmpty())
            return LearningFileEntity
                .builder()
                .bucketName(file.getBucket())
                .fileName(file.getName())
                .status(CsvProcessingFileStatus.NEW.toString())
                .build();

          return null;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Transactional
  List<LearningFileEntity> saveNonProcessedFiles(List<LearningFileEntity> files) {
    return files.stream().map(learningFileRepository::save).collect(toList());
  }

}
