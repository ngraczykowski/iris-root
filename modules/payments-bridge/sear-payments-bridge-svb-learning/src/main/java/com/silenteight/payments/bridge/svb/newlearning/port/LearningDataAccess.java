package com.silenteight.payments.bridge.svb.newlearning.port;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertsReadingResponse;

public interface LearningDataAccess {

  void removeDuplicates();

  AlertsReadingResponse select(long jobId, String fileName);
}
