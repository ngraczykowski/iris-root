package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Alert;

import java.util.List;

public interface AlertRepository {

  void saveAlerts(List<Alert> alert);

  List<Alert> findByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

}
