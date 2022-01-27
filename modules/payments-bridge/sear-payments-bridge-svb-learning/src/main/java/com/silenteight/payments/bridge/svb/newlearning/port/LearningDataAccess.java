package com.silenteight.payments.bridge.svb.newlearning.port;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.LearningProcessedAlert;

import java.util.List;

public interface LearningDataAccess {

  AlertsReadingResponse select(long jobId, String fileName);

  void saveResult(List<LearningProcessedAlert> alerts);
}
