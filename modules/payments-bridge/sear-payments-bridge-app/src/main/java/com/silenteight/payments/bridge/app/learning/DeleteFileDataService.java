package com.silenteight.payments.bridge.app.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.app.batch.JobMaintainer;
import com.silenteight.payments.bridge.data.retention.model.FilesExpiredEvent;

import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobConstants.REMOVE_FILE_DATA_JOB_NAME;

@Service
@Slf4j
@RequiredArgsConstructor
class DeleteFileDataService {

  private final JobMaintainer jobMaintainer;

  @EventListener
  public void removeFileData(FilesExpiredEvent filesExpiredEvent) {
    log.info("Received expired files to remove data = {}", filesExpiredEvent.getFileNames());
    filesExpiredEvent.getFileNames().forEach(this::triggerRemoveJob);
  }

  private void triggerRemoveJob(String fileName) {
    var jobParameters = new JobParametersBuilder()
        .addString(FILE_NAME_PARAMETER, fileName)
        .toJobParameters();
    jobMaintainer.runJobByName(REMOVE_FILE_DATA_JOB_NAME, jobParameters);
  }
}
