package com.silenteight.bridge.core.registration.adapter.incoming.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert;
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.RegistrationMatch;
import com.silenteight.proto.registration.api.v1.*;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

  RegisterAlertsAndMatchesResponse toRegisterAlertsAndMatchesResponse(
      List<RegistrationAlert> alerts) {
    return RegisterAlertsAndMatchesResponse.newBuilder()
        .addAllRegisteredAlertsWithMatches(toRegisteredAlertWithMatches(alerts))
        .build();
  }

  private List<RegisteredAlertWithMatches> toRegisteredAlertWithMatches(
      List<RegistrationAlert> alerts) {
    return alerts.stream()
        .map(this::createRegisteredAlertWithMatches)
        .toList();
  }

  private RegisteredAlertWithMatches createRegisteredAlertWithMatches(
      RegistrationAlert alert) {
    return RegisteredAlertWithMatches.newBuilder()
        .setAlertId(alert.id())
        .setAlertName(Optional.ofNullable(alert.name()).orElse(""))
        .setAlertStatus(AlertStatus.valueOf(alert.status().name()))
        .addAllRegisteredMatches(createRegisteredMatches(alert))
        .build();
  }

  private List<RegisteredMatch> createRegisteredMatches(RegistrationAlert alert) {
    return alert.matches().stream()
        .map(this::createMatch)
        .toList();
  }

  private RegisteredMatch createMatch(RegistrationMatch match) {
    return RegisteredMatch.newBuilder()
        .setMatchId(match.id())
        .setMatchName(Optional.ofNullable(match.name()).orElse(""))
        .build();
  }

  private RegisterAlertsCommand.AlertWithMatches toRegisterAlertsCommandAlertWithMatches(
      AlertWithMatches alertWithMatches) {
    return RegisterAlertsCommand.AlertWithMatches.builder()
        .alertId(alertWithMatches.getAlertId())
        .alertStatus(toRegisterAlertsCommandAlertStatus(alertWithMatches.getStatus()))
        .alertMetadata(alertWithMatches.getAlertMetadata())
        .matches(alertWithMatches.getMatchesList().stream()
            .map(this::toRegisterAlertsCommandMatch)
            .toList())
        .errorDescription(alertWithMatches.getErrorDescription())
        .build();
  }

  private RegisterAlertsCommand.Match toRegisterAlertsCommandMatch(Match match) {
    return new RegisterAlertsCommand.Match(match.getMatchId());
  }

  private RegisterAlertsCommand.AlertStatus toRegisterAlertsCommandAlertStatus(AlertStatus status) {
    return switch (status) {
      case SUCCESS -> RegisterAlertsCommand.AlertStatus.SUCCESS;
      case FAILURE, UNSPECIFIED, UNRECOGNIZED -> RegisterAlertsCommand.AlertStatus.FAILURE;
    };
  }
}
