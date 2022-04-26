package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.ProcessUdsFedAlertsCommand;
import com.silenteight.bridge.core.registration.domain.command.ProcessUdsFedAlertsCommand.FeedingStatus;
import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;
import com.silenteight.bridge.core.registration.domain.strategy.BatchStrategyFactory;
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.PROCESSING;
import static com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus.STORED;

@Slf4j
@Service
@EnableConfigurationProperties(RegistrationAnalysisProperties.class)
@RequiredArgsConstructor
class UdsFedAlertsService {

  private static final Set<BatchStatus> PROCESSABLE_BATCH_STATUSES = EnumSet.of(STORED, PROCESSING);

  private final BatchRepository batchRepository;
  private final AlertRepository alertRepository;
  private final BatchStrategyFactory batchStrategyFactory;

  void processUdsFedAlerts(List<ProcessUdsFedAlertsCommand> commands) {
    groupByBatchId(commands).forEach(this::processBatchAlerts);
  }

  private Map<String, List<ProcessUdsFedAlertsCommand>> groupByBatchId(
      List<ProcessUdsFedAlertsCommand> commands) {
    return commands.stream()
        .collect(Collectors.groupingBy(ProcessUdsFedAlertsCommand::batchId));
  }

  private void processBatchAlerts(String batchId, List<ProcessUdsFedAlertsCommand> commands) {
    batchRepository.findById(batchId)
        .filter(this::hasProcessableStatus)
        .map(this::setStatusToProcessing)
        .ifPresentOrElse(
            batch -> handleCommandsWithAlertsToAnalysis(batch, commands),
            () -> logBatchError(batchId)
        );
  }

  private boolean hasProcessableStatus(Batch batch) {
    if (PROCESSABLE_BATCH_STATUSES.contains(batch.status())) {
      return true;
    }
    log.error(
        "Adding alerts to analysis failed for the batch id [{}]. "
            + "Expected batch status [{}], got [{}].",
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

  private void handleCommandsWithAlertsToAnalysis(
      Batch batch, List<ProcessUdsFedAlertsCommand> commands) {
    final var failedAlerts = getFailedCommands(commands);
    if (CollectionUtils.isNotEmpty(failedAlerts)) {
      handleCommandsWithFailedAlerts(batch, failedAlerts);
    }

    final var succeededAlerts = getSucceededCommands(commands);
    if (CollectionUtils.isNotEmpty(succeededAlerts)) {
      handleCommandsWithSucceededAlerts(batch, succeededAlerts);
    }
  }

  private void handleCommandsWithSucceededAlerts(
      Batch batch, List<ProcessUdsFedAlertsCommand> commands) {
    final var alertNames = extractAlertNamesFromCommands(commands);
    batchStrategyFactory.getStrategyForUdsFedAlertsProcessor(batch)
        .processUdsFedAlerts(batch, alertNames);
  }

  private void handleCommandsWithFailedAlerts(
      Batch batch, List<ProcessUdsFedAlertsCommand> commands) {
    var errorDescriptionsWithAlertNames =
        extractErrorDescriptionsWithAlertNamesFromCommands(commands);
    alertRepository.updateStatusToError(batch.id(), errorDescriptionsWithAlertNames);
  }

  private List<ProcessUdsFedAlertsCommand> getSucceededCommands(
      List<ProcessUdsFedAlertsCommand> commands) {
    return commands.stream()
        .filter(command -> FeedingStatus.SUCCESS == command.feedingStatus())
        .toList();
  }

  private List<ProcessUdsFedAlertsCommand> getFailedCommands(
      List<ProcessUdsFedAlertsCommand> commands) {
    return commands.stream()
        .filter(command -> FeedingStatus.FAILURE == command.feedingStatus())
        .toList();
  }

  private void logBatchError(String batchId) {
    log.error("Batch with id [{}] was either not found or has incorrect status.", batchId);
  }

  private List<String> extractAlertNamesFromCommands(List<ProcessUdsFedAlertsCommand> commands) {
    return commands.stream()
        .map(ProcessUdsFedAlertsCommand::alertName)
        .toList();
  }

  private Map<String, Set<String>> extractErrorDescriptionsWithAlertNamesFromCommands(
      List<ProcessUdsFedAlertsCommand> commands) {
    return commands.stream()
        .collect(Collectors.groupingBy(
                ProcessUdsFedAlertsCommand::errorDescription,
                Collectors.mapping(ProcessUdsFedAlertsCommand::alertName, Collectors.toSet())
            )
        );
  }
}
