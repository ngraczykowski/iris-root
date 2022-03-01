package com.silenteight.payments.bridge.app.learning;

import com.silenteight.payments.bridge.svb.learning.domain.CsvProcessingFileStatus;
import com.silenteight.payments.bridge.svb.learning.domain.LearningFile;

import java.util.Optional;

public interface LearningDataAccess {

  Optional<LearningFile> findLearningFileByNameAndBucketName(String fileName, String bucketName);

  void updateFileStatus(Long fileId, CsvProcessingFileStatus status);
}
