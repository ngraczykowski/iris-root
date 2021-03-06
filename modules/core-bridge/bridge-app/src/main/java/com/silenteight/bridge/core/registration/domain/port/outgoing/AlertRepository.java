package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId;
import com.silenteight.bridge.core.registration.domain.model.*;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface AlertRepository {

  void saveAlerts(List<Alert> alert);

  void updateStatusToRecommended(String batchId, List<String> alertNames);

  void updateStatusToProcessing(
      String batchId, List<String> alertNames, EnumSet<AlertStatus> statusesNotIn);

  void updateStatusToUdsFed(String batchId, List<String> alertNames);

  void updateStatusToError(
      String batchId, Map<String, Set<String>> errorDescriptionsWithAlertNames,
      EnumSet<AlertStatus> statusesNotIn);

  void updateStatusToDelivered(String batchId, List<String> alertNames);

  void updateStatusToDelivered(String batchId);

  List<AlertName> findNamesByBatchIdAndStatusIsRegisteredOrProcessing(String batchId);

  List<Alert> findAllByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchId(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchIdAndStatusIsNotRegistered(String batchId);

  List<AlertWithMatches> findAllWithMatchesByBatchIdAndNameInAndStatusIsNotRegistered(
      String batchId, List<String> alertNames);

  Stream<AlertWithoutMatches> streamAllByBatchIdAndStatusIsNotRegistered(String batchId);

  Stream<AlertWithoutMatches> streamAllByBatchIdAndNameInAndStatusIsNotRegistered(
      String batchId, List<String> alertNames);

  List<MatchWithAlertId> findMatchesByAlertIdIn(Set<Long> alertIds);

  List<AlertWithMatches> findAllWithMatchesByBatchIdAndAlertIdsIn(
      String batchId, List<String> alertIds);

  List<Alert> findAllByBatchIdAndNameIn(String batchId, List<String> alertNames);

  long countAllAlerts(String batchId);

  long countAllCompleted(String batchId);

  long countAllErroneousAlerts(String batchId);

  long countAllDeliveredAndErrorAlerts(String batchId);

  long countAllUdsFedAndErrorAlerts(String batchId);

  List<AlertToRetention> findAlertsApplicableForDataRetention(Instant expirationDate);

  void markAsArchivedAndClearMetadata(List<Long> alertPrimaryIds);
}
