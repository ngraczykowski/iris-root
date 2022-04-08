package com.silenteight.bridge.core.recommendation.adapter.outgoing.crossmodule;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertStatus;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto;
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto.AlertWithMatchesDto.MatchDto;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService;
import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.GetBatchWithAlertsCommand;
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches;
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class RegistrationServiceAdapter implements RegistrationService {

  private final RegistrationFacade registrationFacade;

  @Override
  public BatchWithAlertsDto getBatchWithAlerts(String analysisName) {
    var command = new GetBatchWithAlertsCommand(analysisName);
    var batchWithAlerts = registrationFacade.getBatchWithAlerts(command);
    return toBatchWithAlertsDto(batchWithAlerts);
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
