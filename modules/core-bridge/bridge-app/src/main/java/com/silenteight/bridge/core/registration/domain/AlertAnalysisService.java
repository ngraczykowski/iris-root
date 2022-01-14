package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.Alert.Status;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.model.Match;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.MatchRepository;
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties;

import com.google.protobuf.Timestamp;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.PROCESSING;
import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.STORED;

@Service
@Slf4j
@EnableConfigurationProperties(RegistrationAnalysisProperties.class)
@RequiredArgsConstructor
class AlertAnalysisService {

  private static final Set<BatchStatus> PROCESSABLE_BATCH_STATUSES = EnumSet.of(STORED, PROCESSING);

  private final BatchRepository batchRepository;
  private final AnalysisService analysisService;
  private final RegistrationAnalysisProperties analysisProperties;
  private final AlertRepository alertRepository;
  private final MatchRepository matchRepository;

  public void addAlertsToAnalysis(List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    groupByBatchId(addAlertToAnalysisCommands).forEach(this::processBatchAlerts);
  }

  private void processBatchAlerts(
      String batchId, List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    batchRepository.findById(batchId)
        .filter(this::hasProcessableStatus)
        .map(this::setStatusToProcessing)
        .ifPresentOrElse(
            batch -> addBatchAlertsToAnalysis(batch, addAlertToAnalysisCommands),
            () -> logBatchError(batchId)
        );
  }

  private boolean hasProcessableStatus(Batch batch) {
    if (PROCESSABLE_BATCH_STATUSES.contains(batch.status())) {
      return true;
    }
    log.error(
        "Adding alerts to analysis failed for the batch: {}. "
            + "Expected batch status: {}, got {}",
        batch.id(),
        PROCESSABLE_BATCH_STATUSES,
        batch.status());
    return false;
  }

  private Batch setStatusToProcessing(Batch batch) {
    if (PROCESSING != batch.status()) {
      batchRepository.updateStatus(batch.id(), PROCESSING);
    }
    return batch;
  }

  private void addBatchAlertsToAnalysis(
      Batch batch, List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    final var alertIds = addAlertToAnalysisCommands.stream()
        .map(AddAlertToAnalysisCommand::alertId)
        .toList();
    final var alerts = alertRepository.findAllByBatchIdAndAlertIdIn(batch.id(), alertIds);

    final var alertNames = alerts.stream()
        .map(Alert::name)
        .toList();
    analysisService.addAlertsToAnalysis(
        batch.analysisName(),
        alertNames,
        getAlertDeadlineTime()
    );

    updateAlertsAndMatchesStatuses(batch, alertIds, alerts, addAlertToAnalysisCommands);
  }

  private void updateAlertsAndMatchesStatuses(
      Batch batch, List<String> alertIds, List<Alert> alerts,
      List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    alertRepository.updateStatusByBatchIdAndAlertIdIn(Status.PROCESSING, batch.id(), alertIds);

    updateMatchesStatuses(alerts, addAlertToAnalysisCommands);
  }

  private void updateMatchesStatuses(
      List<Alert> alerts, List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {

    final Map<Match.Status, List<String>> matchStatusNamesMap = Map.of(
        Match.Status.PROCESSING, new ArrayList<>(),
        Match.Status.ERROR, new ArrayList<>()
    );

    addAlertToAnalysisCommands
        .forEach(command -> alerts.stream()
            .filter(alert -> command.alertId().equals(alert.alertId()))
            .findFirst()
            .ifPresentOrElse(
                alert -> addMatchNameToNewStatusesMap(alert, command, matchStatusNamesMap),
                () -> log.error(
                    "Could not update alert matches. Alert with ID {} could not be found",
                    command.alertId())
            ));

    matchStatusNamesMap.forEach((status, matchNames) -> {
      if (CollectionUtils.isNotEmpty(matchNames)) {
        matchRepository.updateStatusByNameIn(status, matchNames);
      }
    });
  }

  private void addMatchNameToNewStatusesMap(
      Alert alert, AddAlertToAnalysisCommand command,
      Map<Match.Status, List<String>> matchStatusNamesMap) {
    alert.matches()
        .forEach(match -> {
          var existsInCommand = command.matchIds().contains(match.matchId());
          var newStatus = existsInCommand ? Match.Status.PROCESSING : Match.Status.ERROR;
          matchStatusNamesMap.get(newStatus).add(match.name());
        });
  }

  private void logBatchError(String batchId) {
    log.error("Batch with id {} was either not found or has incorrect status", batchId);
  }

  private Map<String, List<AddAlertToAnalysisCommand>> groupByBatchId(
      List<AddAlertToAnalysisCommand> addAlertToAnalysisCommands) {
    return addAlertToAnalysisCommands.stream()
        .collect(Collectors.groupingBy(AddAlertToAnalysisCommand::batchId));
  }

  private Timestamp getAlertDeadlineTime() {
    var alertTtl = analysisProperties.alertTtl();
    var offsetDateTime = OffsetDateTime.now().plusSeconds(alertTtl.getSeconds());
    return Timestamp.newBuilder()
        .setSeconds(offsetDateTime.toEpochSecond())
        .setNanos(offsetDateTime.getNano())
        .build();
  }
}
