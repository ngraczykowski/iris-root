package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.AlertStatus
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.Match
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.RegistrationMatch
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.Status

import java.time.OffsetDateTime
import java.util.stream.Collectors

class AlertServiceSpecHelper {

  static def PRIORITY = 1

  static AlertWithMatches buildAlert(String id, List<String> matchesIds, AlertStatus status) {
    AlertWithMatches.builder()
        .alertId(id)
        .matches(buildMatches(matchesIds))
        .alertStatus(status)
        .build()
  }

  static AlertsToRegister.AlertWithMatches buildAlertToRegister(
      String id,
      List<String> matchesIds) {
    AlertsToRegister.AlertWithMatches.builder()
        .alertId(id)
        .priority(PRIORITY)
        .matches(buildMatchesToRegister(matchesIds))
        .alertTime(OffsetDateTime.now())
        .build()
  }

  static com.silenteight.bridge.core.registration.domain.model.AlertWithMatches buildRegisteredAlert(
      String id) {
    com.silenteight.bridge.core.registration.domain.model.AlertWithMatches.builder()
        .id(id)
        .name('alertName')
        .metadata('alertMetadata')
        .matches(
            [
                new com.silenteight.bridge.core.registration.domain.model.AlertWithMatches.Match(
                    'match_id_11', 'match_name')
            ]
        )
        .status(com.silenteight.bridge.core.registration.domain.model.AlertStatus.REGISTERED)
        .build()
  }

  static RegistrationAlert buildRegistrationAlert(String id, Status status) {
    RegistrationAlert.builder()
        .id(id)
        .name('alertName')
        .status(status)
        .matches(
            [
                RegistrationMatch.builder()
                    .id('match_id_11')
                    .name('match_name')
                    .build()
            ]
        )
        .build()
  }

  private static List<Match> buildMatches(List<String> matchesIds) {
    matchesIds.stream()
        .map(matchId -> new Match(matchId))
        .collect(Collectors.toList())
  }

  private static List<AlertsToRegister.Match> buildMatchesToRegister(List<String> matchesIds) {
    matchesIds.stream()
        .map(id -> new AlertsToRegister.Match(id))
        .collect(Collectors.toList())
  }
}
