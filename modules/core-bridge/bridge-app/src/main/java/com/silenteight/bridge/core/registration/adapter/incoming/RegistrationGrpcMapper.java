package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand;
import com.silenteight.proto.registration.api.v1.AlertStatus;
import com.silenteight.proto.registration.api.v1.AlertWithMatches;
import com.silenteight.proto.registration.api.v1.Match;
import com.silenteight.proto.registration.api.v1.RegisterAlertsAndMatchesRequest;

import org.springframework.stereotype.Component;

@Slf4j
@Component
class RegistrationGrpcMapper {

  RegisterAlertsCommand toRegisterAlertsCommand(RegisterAlertsAndMatchesRequest request) {
    return new RegisterAlertsCommand(
        request.getBatchId(),
        request.getAlertsWithMatchesList().stream()
            .map(this::toRegisterAlertsCommandAlertWithMatches)
            .toList());
  }

  private RegisterAlertsCommand.AlertWithMatches toRegisterAlertsCommandAlertWithMatches(
      AlertWithMatches alertWithMatches) {

    return RegisterAlertsCommand.AlertWithMatches.builder()
        .alertId(alertWithMatches.getAlertId())
        .alertStatus(toRegisterAlertsCommandAlertStatus(alertWithMatches.getStatus()))
        .matches(alertWithMatches.getMatchesList().stream()
            .map(this::toRegisterAlertsCommandMatch)
            .toList())
        .errorDescription(alertWithMatches.getErrorDescription())
        .build();
  }

  private RegisterAlertsCommand.Match toRegisterAlertsCommandMatch(Match match) {
    return new RegisterAlertsCommand.Match(match.getMatchId());
  }

  private RegisterAlertsCommand.AlertStatus toRegisterAlertsCommandAlertStatus(
      AlertStatus alertStatus) {

    switch (alertStatus) {
      case SUCCESS -> {
        return RegisterAlertsCommand.AlertStatus.SUCCESS;
      }
      case FAILURE -> {
        return RegisterAlertsCommand.AlertStatus.FAILURE;
      }
      default -> {
        log.warn("Unknown alert status in mapping from gRPC response: {}", alertStatus);
        return RegisterAlertsCommand.AlertStatus.FAILURE;
      }
    }
  }
}
