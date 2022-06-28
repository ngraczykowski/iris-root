package com.silenteight.bridge.core.recommendation.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.BatchPriorityDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId;
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface RegistrationService {

  BatchWithAlertsDto getBatchWithAlerts(String analysisName, List<String> alertNames);

  BatchIdWithPolicy getBatchId(String analysisName);

  BatchPriorityDto getBatchPriority(String analysisName);

  boolean isSimulationBatch(String analysisName);

  Stream<AlertWithoutMatches> streamAllAlerts(String batchId, List<String> alertsNames);

  List<MatchWithAlertId> getAllMatchesForAlerts(Set<Long> alertsIds);

  Map<AlertStatus, Long> getAlertsStatusStatistics(String batchId, List<String> alertsNames);
}
