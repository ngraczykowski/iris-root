package com.silenteight.payments.bridge.app.batch;

import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningFile;

import java.util.Optional;

public interface LearningDataAccess {

  Optional<LearningFile> findLearningFileByNameAndBucketName(String fileName, String bucketName);

  void updateFileStatus(Long fileId, CsvProcessingFileStatus status);
}
