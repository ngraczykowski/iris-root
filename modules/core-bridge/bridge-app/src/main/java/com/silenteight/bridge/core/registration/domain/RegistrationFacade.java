package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId;
import com.silenteight.bridge.core.registration.domain.command.*;
import com.silenteight.bridge.core.registration.domain.model.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationFacade {

  private final BatchService batchService;
  private final AlertService alertService;
  private final UdsFedAlertsService udsFedAlertsService;
  private final BatchTimeoutService batchTimeoutService;
  private final DataRetentionService dataRetentionService;

  public BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchService.register(registerBatchCommand);
  }

  public List<AlertWithMatches> getAlertsWithMatches(GetAlertsWithMatchesCommand command) {
    return alertService.getAlertsAndMatches(command.batchId());
  }

  public BatchWithAlerts getBatchWithAlerts(GetBatchWithAlertsCommand command) {
    var batchIdWithPolicy =
        batchService.findBatchIdWithPolicyByAnalysisName(command.analysisName());
    var alerts = getAlerts(batchIdWithPolicy.id(), command.alertNames());

    return BatchWithAlerts.builder()
        .batchId(batchIdWithPolicy.id())
        .policyId(batchIdWithPolicy.policyId())
        .alerts(alerts)
        .build();
  }

  public void notifyBatchError(NotifyBatchErrorCommand notifyBatchErrorCommand) {
    batchService.notifyBatchError(notifyBatchErrorCommand);
  }

  public void markAlertsAsRecommended(MarkAlertsAsRecommendedCommand command) {
    var batch = batchService.findBatchByAnalysisName(command.analysisName());

    log.info(
        "Batch for given analysis name [{}], has batch id [{}].",
        command.analysisName(), batch.id());

    alertService.updateStatusToRecommended(batch.id(), command.alertNames());

    if (alertService.hasNoPendingAlerts(batch) || command.isTimedOut()) {
      log.info("Completing batch, without pending alerts, with id [{}]", batch.id());
      batchService.completeBatch(batch);
    }
  }

  public List<RegistrationAlert> registerAlertsAndMatches(RegisterAlertsCommand command) {
    var batch = batchService.findPendingBatch(command.batchId());

    return alertService.registerAlertsAndMatches(command, batch.priority());
  }

  public void markAlertsAsDelivered(MarkAlertsAsDeliveredCommand command) {
    var batch = getBatch(command);
    alertService.updateStatusToDelivered(batch.id(), command.alertNames());
    if (allAlertsAreDelivered(batch, command.alertNames())) {
      batchService.markBatchAsDelivered(batch);
    }
  }

  public void processUdsFedAlerts(List<ProcessUdsFedAlertsCommand> processUdsFedAlertsCommands) {
    udsFedAlertsService.processUdsFedAlerts(processUdsFedAlertsCommands);
  }

  public void verifyBatchTimeout(VerifyBatchTimeoutCommand command) {
    batchTimeoutService.verifyBatchTimeout(command);
  }

  public void verifyBatchTimeoutForAllErroneousAlerts(VerifyBatchTimeoutCommand command) {
    batchTimeoutService.verifyBatchTimeoutForAllErroneousAlerts(command);
  }

  public BatchIdWithPolicy getBatchId(GetBatchIdCommand command) {
    return batchService.findBatchIdWithPolicyByAnalysisName(command.analysisName());
  }

  public Stream<AlertWithoutMatches> streamAllByBatchId(GetAlertsWithoutMatchesCommand command) {
    if (command.alertsName().isEmpty()) {
      return alertService.streamAllByBatchId(command.batchId());
    }
    return alertService.streamAllByBatchIdAndNameIn(command.batchId(), command.alertsName());
  }

  public List<MatchWithAlertId> getAllMatchesForAlerts(GetMatchesWithAlertIdCommand command) {
    return alertService.getAllMatchesForAlerts(command.alertsIds());
  }

  public Map<BatchWithAlertsDto.AlertStatus, Long> getAlertsStatusStatistics(
      GetAlertStatisticsCommand command) {
    if (command.alertsNames().isEmpty()) {
      return batchService.getAlertsStatusStatistics(command.batchId());
    }
    return batchService.getAlertsStatusStatistics(command.batchId(), command.alertsNames());
  }

  public void startDataRetention(StartDataRetentionCommand command) {
    dataRetentionService.start(command);
  }

  private boolean allAlertsAreDelivered(Batch batch, List<String> alertNames) {
    if (CollectionUtils.isEmpty(alertNames)) {
      return true;
    } else {
      return alertService.hasAllDeliveredAlerts(batch);
    }
  }

  private Batch getBatch(MarkAlertsAsDeliveredCommand command) {
    if (StringUtils.isNotEmpty(command.batchId())) {
      return batchService.findBatchById(command.batchId());
    } else if (StringUtils.isNotEmpty(command.analysisName())) {
      return batchService.findBatchByAnalysisName(command.analysisName());
    } else {
      throw new IllegalStateException("Either batchId or analysisName must be present.");
    }
  }

  private List<AlertWithMatches> getAlerts(String batchId, List<String> alertNames) {
    if (!alertNames.isEmpty()) {
      return alertService.getAlertsAndMatchesByName(batchId, alertNames);
    }
    return alertService.getAlertsAndMatches(batchId);
  }
}
