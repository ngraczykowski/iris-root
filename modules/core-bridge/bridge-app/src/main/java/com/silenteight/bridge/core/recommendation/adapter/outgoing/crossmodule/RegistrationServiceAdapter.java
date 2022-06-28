package com.silenteight.bridge.core.recommendation.adapter.outgoing.crossmodule;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.BatchPriorityDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto.MatchDto;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId;
import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.*;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy;
import com.silenteight.bridge.core.registration.domain.model.BatchPriority;
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class RegistrationServiceAdapter implements RegistrationService {

  private final RegistrationFacade registrationFacade;

  @Override
  public BatchWithAlertsDto getBatchWithAlerts(String analysisName, List<String> alertNames) {
    var command = new GetBatchWithAlertsCommand(analysisName, alertNames);
    var batchWithAlerts = registrationFacade.getBatchWithAlerts(command);
    return toBatchWithAlertsDto(batchWithAlerts);
  }

  @Override
  public BatchIdWithPolicy getBatchId(String analysisName) {
    var command = new GetBatchIdCommand(analysisName);
    return registrationFacade.getBatchId(command);
  }

  @Override
  public BatchPriorityDto getBatchPriority(String analysisName) {
    var command = new GetBatchPriorityCommand(analysisName);
    return new BatchPriorityDto(Optional.ofNullable(registrationFacade.getBatchPriority(command))
        .map(BatchPriority::priority)
        .orElse(0));
  }

  @Override
  public boolean isSimulationBatch(String analysisName) {
    return registrationFacade.isSimulationBatch(analysisName);
  }

  @Override
  public Stream<AlertWithoutMatches> streamAllAlerts(String batchId, List<String> alertsNames) {
    var command = new GetAlertsWithoutMatchesCommand(batchId, alertsNames);
    return registrationFacade.streamAllByBatchId(command);
  }

  @Override
  public List<MatchWithAlertId> getAllMatchesForAlerts(Set<Long> alertsIds) {
    var command = new GetMatchesWithAlertIdCommand(alertsIds);
    return registrationFacade.getAllMatchesForAlerts(command);
  }

  @Override
  public Map<AlertStatus, Long> getAlertsStatusStatistics(
      String batchId, List<String> alertsNames) {
    return registrationFacade.getAlertsStatusStatistics(
        new GetAlertStatisticsCommand(batchId, alertsNames));
  }

  private BatchWithAlertsDto toBatchWithAlertsDto(BatchWithAlerts batchWithAlerts) {
    return BatchWithAlertsDto.builder()
        .batchId(batchWithAlerts.batchId())
        .policyId(batchWithAlerts.policyId())
        .alerts(toAlertWithMatchesDto(batchWithAlerts.alerts()))
        .build();
  }

  private List<AlertWithMatchesDto> toAlertWithMatchesDto(List<AlertWithMatches> alerts) {
    return alerts.stream()
        .map(alert -> AlertWithMatchesDto.builder()
            .id(alert.id())
            .name(alert.name())
            .status(AlertStatus.valueOf(alert.status().name()))
            .metadata(alert.metadata())
            .errorDescription(alert.errorDescription())
            .matches(toMatchesDto(alert))
            .build())
        .toList();
  }

  private List<MatchDto> toMatchesDto(AlertWithMatches alert) {
    return alert.matches().stream()
        .map(match -> new MatchDto(match.id(), match.name()))
        .toList();
  }
}
