package com.silenteight.scb.ingest.domain

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchDetails
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredAlertWithMatches
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredMatch

import java.time.Instant

import static com.silenteight.scb.ingest.domain.model.RegistrationResponse.builder

class Fixtures {

  static registrationResponse(List<Alert> alerts) {
    builder()
        .registeredAlertWithMatches(
            alerts.stream()
                .map(this::registeredAlertWithMatches)
                .toList())
        .build()
  }

  static registeredAlertWithMatches(Alert alert) {
    RegisteredAlertWithMatches.builder()
        .alertId(alert.id().sourceId())
        .alertName('alertName/' + alert.id().sourceId())
        .registeredMatches(
            alert.matches().stream()
                .map(this::registeredMatch)
                .toList())
        .build()
  }

  static registeredMatch(Match match) {
    RegisteredMatch.builder()
        .matchId(match.id().sourceId())
        .matchName('matchName/' + match.id().sourceId())
        .build()
  }

  static alert(int alertId) {
    Alert.builder()
        .id(
            ObjectId.builder()
                .id(UUID.randomUUID())
                .sourceId('alertId' + alertId)
                .discriminator('discriminator/' + alertId)
                .build())
        .matches(
            [Match.builder()
                 .id(
                     ObjectId.builder()
                         .id(UUID.randomUUID())
                         .sourceId('match/' + alertId)
                         .build())
                 .details(MatchDetails.builder().build())
                 .build()])
        .decisionGroup('decisionGroup')
        .details(
            AlertDetails.builder()
                .systemId('systemId/' + alertId)
                .batchId('batchId')
                .build())
        .decisions(
            [Decision.builder()
                 .solution(AnalystSolution.ANALYST_FALSE_POSITIVE)
                 .createdAt(Instant.now())
                 .comment('comment')
                 .build()])
        .build()
  }
}
