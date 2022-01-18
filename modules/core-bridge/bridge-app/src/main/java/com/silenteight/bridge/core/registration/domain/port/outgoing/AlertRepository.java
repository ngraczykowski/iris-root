package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertId;
import com.silenteight.bridge.core.registration.domain.model.AlertStatusStatistics;

import java.util.List;

public interface AlertRepository {

  void saveAlerts(List<Alert> alert);

  void updateStatusToRecommended(String batchId, List<String> alertNames);

  List<AlertId> findAllAlertIdsByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  List<Alert> findAllByBatchId(String batchId);

  List<Alert> findAllByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  void updateStatusByBatchIdAndAlertIdIn(
      Alert.Status status, String batchId, List<String> alertIds);

  long countAllPendingAlerts(String batchId);

  AlertStatusStatistics countAlertsByStatusForBatchId(String batchId);
}
