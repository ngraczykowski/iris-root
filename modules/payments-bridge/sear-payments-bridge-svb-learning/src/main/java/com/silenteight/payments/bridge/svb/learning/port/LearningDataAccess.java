package com.silenteight.payments.bridge.svb.learning.port;

import com.silenteight.payments.bridge.svb.learning.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.learning.step.etl.LearningProcessedAlert;

import java.util.List;

public interface LearningDataAccess {

  AlertsReadingResponse select(long jobId, String fileName);

  void saveResult(List<LearningProcessedAlert> alerts);

  boolean isFileStored(String fileName);

  void removeDuplicates();

  void removeCsvRows(List<Long> rowIds);

  void removeAlerts(List<Long> alertIds);

  void removeHits(List<Long> hitIds);

  void removeActions(List<Long> actionIds);
}
