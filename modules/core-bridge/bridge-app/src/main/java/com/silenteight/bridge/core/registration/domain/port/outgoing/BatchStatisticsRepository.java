package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus;

import java.util.List;
import java.util.Map;

public interface BatchStatisticsRepository {

  Map<AlertStatus, Long> getAlertsStatusStatistics(String batchId);

  Map<AlertStatus, Long> getAlertsStatusStatistics(String batchId, List<String> alertsNames);
}
