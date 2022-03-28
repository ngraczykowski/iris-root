package com.silenteight.scb.ingest.fixtures

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.domain.model.*
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredAlertWithMatches
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredMatch

class Fixtures {

  static BatchMetadata BATCH_METADATA = new BatchMetadata()

  static def BATCH = Batch.builder()
      .id('batchId')
      .alertCount(3L)
      .metadata(BATCH_METADATA)
      .build()

  static def MATCH = Match.builder()
      .id('matchId')
      .build()

  static def matchId = ObjectId.builder()
      .sourceId('matchId')
      .build()

  static def MATCH_ALERT = com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match
      .builder()
      .id(matchId)
      .build()

  static def ALERT_WITH_MATCHES_METADATA = AlertWithMatches.builder()
      .alertId('alertId')
      .status(AlertStatus.SUCCESS)
      .metadata(ALERT_METADATA)
      .matches([MATCH])
      .build()

  static AlertMetadata ALERT_METADATA = AlertMetadata.builder()
      .watchlistId("watchlistId")
      .discriminator("discriminator")
      .build()

  static def REGISTERED_ALERT_WITH_MATCHES = RegisteredAlertWithMatches.builder()
      .alertId('alertId')
      .alertName('alertName')
      .alertStatus(AlertStatus.SUCCESS)
      .registeredMatches(
          [
              RegisteredMatch.builder()
                  .matchId('matchId')
                  .matchName('matchName')
                  .build()
          ]
      )
      .build()

  static def REGISTRATION_REQUEST = RegistrationRequest
      .of(BATCH.id(), [ALERT_WITH_MATCHES_METADATA, ALERT_WITH_MATCHES_METADATA])

  static REGISTRATION_RESPONSE = RegistrationResponse.builder()
      .registeredAlertWithMatches([REGISTERED_ALERT_WITH_MATCHES, REGISTERED_ALERT_WITH_MATCHES])
      .build()
}
