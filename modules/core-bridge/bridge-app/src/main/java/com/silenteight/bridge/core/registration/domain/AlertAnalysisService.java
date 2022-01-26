package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.AddAlertToAnalysisCommand.FeedingStatus;
import com.silenteight.bridge.core.registration.domain.model.AlertName;
import com.silenteight.bridge.core.registration.domain.model.AlertStatus;
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

  public void addAlertsToAnalysis(List<AddAlertToAnalysisCommand> commands) {
    groupByBatchId(commands).forEach(this::processBatchAlerts);
  }

  private Map<String, List<AddAlertToAnalysisCommand>> groupByBatchId(
      List<AddAlertToAnalysisCommand> commands) {
    return commands.stream()
        .collect(Collectors.groupingBy(AddAlertToAnalysisCommand::batchId));
  }

  private void processBatchAlerts(String batchId, List<AddAlertToAnalysisCommand> commands) {
    batchRepository.findById(batchId)
        .filter(this::hasProcessableStatus)
        .map(this::setStatusToProcessing)
        .ifPresentOrElse(
            batch -> addBatchAlertsToAnalysis(batch, commands),
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

  private void addBatchAlertsToAnalysis(Batch batch, List<AddAlertToAnalysisCommand> commands) {
    if (containsAllSucceededAlerts(commands)) {
      if (containsAllSucceededMatches(commands)) {
        handleCommandsWithAllSucceededAlertsAndMatches(batch, commands);
      } else {
        handleCommandsWithAllSucceededAlertsAndMixedMatches(batch, commands);
      }
    } else {
      handleCommandsWithMixedAlerts(batch, commands);
    }
  }

  private boolean containsAllSucceededAlerts(List<AddAlertToAnalysisCommand> commands) {
    return commands.stream()
        .allMatch(alert -> FeedingStatus.SUCCESS.equals(alert.feedingStatus()));
  }

  private boolean containsAllSucceededMatches(
      List<AddAlertToAnalysisCommand> commands) {
    return commands.stream()
        .flatMap(command -> command.fedMatches().stream())
        .allMatch(fedMatch -> FeedingStatus.SUCCESS.equals(fedMatch.feedingStatus()));
  }

  private void handleCommandsWithAllSucceededAlertsAndMatches(
      Batch batch, List<AddAlertToAnalysisCommand> commands) {
    final var alertIds = extractAlertIdsFromCommands(commands);
    alertRepository.updateStatusToProcessing(batch.id(), alertIds);
    addAlertsToAnalysis(batch, alertIds);
  }

  private void handleCommandsWithAllSucceededAlertsAndMixedMatches(
      Batch batch, List<AddAlertToAnalysisCommand> commands) {
    final var alertIds = extractAlertIdsFromCommands(commands);
    alertRepository.updateStatusByBatchIdAndAlertIdIn(AlertStatus.PROCESSING, batch.id(), alertIds);
    updateMatchesStatuses(batch, commands);
    addAlertsToAnalysis(batch, alertIds);
  }

  private void handleCommandsWithMixedAlerts(
      Batch batch, List<AddAlertToAnalysisCommand> commands) {
    final var failedAlerts = getFailedCommands(commands);
    if (CollectionUtils.isNotEmpty(failedAlerts)) {
      handleCommandsWithFailedAlerts(batch, failedAlerts);
    }

    final var succeededAlerts = getSucceededCommands(commands);
    if (CollectionUtils.isNotEmpty(succeededAlerts)) {
      handleCommandsWithAllSucceededAlertsAndMixedMatches(batch, succeededAlerts);
    }
  }

  private void handleCommandsWithFailedAlerts(
      Batch batch, List<AddAlertToAnalysisCommand> commands) {
    final var alertIds = extractAlertIdsFromCommands(commands);
    alertRepository.updateStatusToError(batch.id(), alertIds);
  }

  private void addAlertsToAnalysis(Batch batch, List<String> alertIds) {
    final var alertNames =
        alertRepository.findAllAlertNamesByBatchIdAndAlertIdIn(batch.id(), alertIds).stream()
            .map(AlertName::alertName)
            .toList();
    analysisService.addAlertsToAnalysis(
        batch.analysisName(),
        alertNames,
        getAlertDeadlineTime()
    );
  }

  private List<AddAlertToAnalysisCommand> getSucceededCommands(
      List<AddAlertToAnalysisCommand> commands) {
    return commands.stream()
        .filter(command -> FeedingStatus.SUCCESS.equals(command.feedingStatus()))
        .toList();
  }

  private List<AddAlertToAnalysisCommand> getFailedCommands(
      List<AddAlertToAnalysisCommand> commands) {
    return commands.stream()
        .filter(command -> FeedingStatus.FAILURE.equals(command.feedingStatus()))
        .toList();
  }

  private void updateMatchesStatuses(Batch batch, List<AddAlertToAnalysisCommand> commands) {
    final Map<Match.Status, List<String>> matchStatusNamesMap = Map.of(
        Match.Status.PROCESSING, new ArrayList<>(),
        Match.Status.ERROR, new ArrayList<>()
    );
    final var alertIds = new ArrayList<String>();

    commands.forEach(command -> {
      alertIds.add(command.alertId());
      command.fedMatches().forEach(fedMatch -> {
        if (FeedingStatus.SUCCESS.equals(fedMatch.feedingStatus())) {
          matchStatusNamesMap.get(Match.Status.PROCESSING).add(fedMatch.id());
        } else {
          matchStatusNamesMap.get(Match.Status.ERROR).add(fedMatch.id());
        }
      });
    });

    matchStatusNamesMap.forEach((status, matchIds) -> {
      if (CollectionUtils.isNotEmpty(matchIds)) {
        matchRepository.updateStatusByBatchIdAndMatchIdInAndExternalAlertIdIn(
            status, batch.id(), matchIds, alertIds);
      }
    });
  }

  private void logBatchError(String batchId) {
    log.error("Batch with id {} was either not found or has incorrect status", batchId);
  }

  private Timestamp getAlertDeadlineTime() {
    var alertTtl = analysisProperties.alertTtl();
    var offsetDateTime = OffsetDateTime.now().plusSeconds(alertTtl.getSeconds());
    return Timestamp.newBuilder()
        .setSeconds(offsetDateTime.toEpochSecond())
        .setNanos(offsetDateTime.getNano())
        .build();
  }

  private List<String> extractAlertIdsFromCommands(List<AddAlertToAnalysisCommand> commands) {
    return commands.stream()
        .map(AddAlertToAnalysisCommand::alertId)
        .toList();
  }
}
