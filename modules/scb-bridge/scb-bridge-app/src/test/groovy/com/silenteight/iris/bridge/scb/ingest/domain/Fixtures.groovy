/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain

import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationResponse.RegisteredAlertWithMatches
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationResponse.RegisteredMatch

import java.time.Instant

import static com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationResponse.builder

class Fixtures {

  static registrationResponse(List<com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert> alerts) {
    builder()
        .registeredAlertWithMatches(
            alerts.stream()
                .map(this::registeredAlertWithMatches)
                .toList())
        .build()
  }

  static registeredAlertWithMatches(com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert alert) {
    RegisteredAlertWithMatches.builder()
        .alertId(alert.id().sourceId())
        .alertName('alertName/' + alert.id().sourceId())
        .registeredMatches(
            alert.matches().stream()
                .map(this::registeredMatch)
                .toList())
        .build()
  }

  static registeredMatch(com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match match) {
    RegisteredMatch.builder()
        .matchId(match.id().sourceId())
        .matchName('matchName/' + match.id().sourceId())
        .build()
  }

  static alert(int alertId) {
    com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.builder()
        .id(
            com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId.builder()
                .id(UUID.randomUUID())
                .sourceId('alertId' + alertId)
                .discriminator('discriminator/' + alertId)
                .build())
        .matches(
            [com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match.builder()
                 .id(
                     com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId.builder()
                         .id(UUID.randomUUID())
                         .sourceId('match/' + alertId)
                         .build())
                 .details(com
                     .silenteight
                     .iris
                     .bridge.scb.ingest.adapter.incomming.common.model.match.MatchDetails.builder().build())
                 .build()])
        .decisionGroup('decisionGroup')
        .details(
            com
                .silenteight
                .iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails.builder()
                .systemId('systemId/' + alertId)
                .batchId('batchId')
                .build())
        .decisions(
            [com
                 .silenteight
                 .iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision.builder()
                 .solution(com
                     .silenteight
                     .iris
                     .bridge
                     .scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution.ANALYST_FALSE_POSITIVE)
                 .createdAt(Instant.now())
                 .comment('comment')
                 .build()])
        .build()
  }
}
