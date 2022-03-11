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

  void updateStatusToProcessing(String batchId, List<String> alertIds);

  void updateStatusToError(
      String batchId, Map<String, Set<String>> errorDescriptionsWithAlertIds);

  List<AlertName> findAllAlertNamesByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  List<AlertName> findNamesByBatchIdAndStatusIsRegisteredOrProcessing(String batchId);

  List<Alert> findAllByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchIdAndAlertIdsIn(
      String batchId, List<String> alertIds);

  List<Alert> findAllByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  long countAllPendingAlerts(String batchId);

  long countAllErroneousAlerts(String batchId);
}
