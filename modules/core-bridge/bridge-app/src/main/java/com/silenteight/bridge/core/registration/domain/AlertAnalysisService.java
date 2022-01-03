package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.model.Batch;
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties;

import com.google.protobuf.Timestamp;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    analysisService.addAlertsToAnalysis(
        batch.analysisName(),
        addAlertToAnalysisCommands,
        getAlertDeadlineTime()
    );
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
