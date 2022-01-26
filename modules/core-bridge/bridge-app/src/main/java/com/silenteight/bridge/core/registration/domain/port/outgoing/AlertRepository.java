package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.*;

import java.util.List;

public interface AlertRepository {

  void saveAlerts(List<Alert> alert);

  void updateStatusToRecommended(String batchId, List<String> alertNames);

  void updateStatusToProcessing(String batchId, List<String> alertIds);

  void updateStatusToError(String batchId, List<String> alertIds);

  List<AlertName> findAllAlertNamesByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  List<Alert> findAllByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchIdAndAlertIdsIn(
      String batchId, List<String> alertIds);

  List<Alert> findAllByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  void updateStatusByBatchIdAndAlertIdIn(
      AlertStatus status, String batchId, List<String> alertIds);

  long countAllPendingAlerts(String batchId);

  AlertStatusStatistics countAlertsByStatusForBatchId(String batchId);
}
