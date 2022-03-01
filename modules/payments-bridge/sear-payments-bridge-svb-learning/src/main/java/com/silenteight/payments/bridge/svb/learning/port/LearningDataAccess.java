package com.silenteight.payments.bridge.svb.learning.port;

import com.silenteight.payments.bridge.svb.learning.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.learning.step.etl.LearningProcessedAlert;

import java.util.List;

public interface LearningDataAccess {

  AlertsReadingResponse select(long jobId, String fileName);

  void saveResult(List<LearningProcessedAlert> alerts);

  boolean isFileStored(String fileName);

  void removeDuplicates();
}
