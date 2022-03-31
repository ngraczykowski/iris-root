package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertName;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AlertRepository {

  void saveAlerts(List<Alert> alert);

  void updateStatusToRecommended(String batchId, List<String> alertNames);

  void updateStatusToProcessing(String batchId, List<String> alertNames);

  void updateStatusToError(
      String batchId, Map<String, Set<String>> errorDescriptionsWithAlertNames);

  void updateStatusToDelivered(String batchId, List<String> alertNames);

  void updateStatusToDelivered(String batchId);

  List<AlertName> findNamesByBatchIdAndStatusIsRegisteredOrProcessing(String batchId);

  List<Alert> findAllByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchIdAndAlertIdsIn(
      String batchId, List<String> alertIds);

  List<Alert> findAllByBatchIdAndNameIn(String batchId, List<String> alertNames);

  long countAllPendingAlerts(String batchId);

  long countAllErroneousAlerts(String batchId);

  long countAllDeliveredAndErrorAlerts(String batchId);
}
