package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertId;
import com.silenteight.bridge.core.registration.domain.model.AlertName;
import com.silenteight.bridge.core.registration.domain.model.AlertStatusStatistics;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;

import java.util.List;

public interface AlertRepository {

  void saveAlerts(List<Alert> alert);

  void updateStatusToRecommended(String batchId, List<String> alertNames);

  void updateStatusToProcessing(String batchId, List<String> alertIds);

  void updateStatusToError(String batchId, List<String> alertIds);

  List<AlertId> findAllAlertIdsByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  List<AlertName> findAllAlertNamesByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  List<Alert> findAllByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchId(String batchId);

  List<Alert> findAllByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  void updateStatusByBatchIdAndAlertIdIn(
      Alert.Status status, String batchId, List<String> alertIds);

  long countAllPendingAlerts(String batchId);

  AlertStatusStatistics countAlertsByStatusForBatchId(String batchId);
}
