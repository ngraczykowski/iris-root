package com.silenteight.payments.bridge.svb.newlearning.port;

import com.silenteight.payments.bridge.svb.newlearning.domain.CsvProcessingFileStatus;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningFile;

import java.util.Optional;

public interface LearningDataAccess {

  Optional<LearningFile> findLearningFileById(Long fileId);

  void updateFileStatus(Long fileId, CsvProcessingFileStatus status);

  void removeDuplicates();
}
