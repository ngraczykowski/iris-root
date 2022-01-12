package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertId;

import java.util.List;

public interface AlertRepository {

  void saveAlerts(List<Alert> alert);

  void updateStatusToRecommended(String batchId, List<String> alertNames);

  List<AlertId> findAllAlertIdsByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  List<Alert> findAllByBatchId(String batchId);

  long countAllPendingAlerts(String batchId);
}
