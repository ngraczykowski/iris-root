/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.fixtures

import com.silenteight.iris.bridge.scb.ingest.domain.model.AlertMetadata
import com.silenteight.iris.bridge.scb.ingest.domain.model.AlertStatus
import com.silenteight.iris.bridge.scb.ingest.domain.model.AlertWithMatches
import com.silenteight.iris.bridge.scb.ingest.domain.model.Batch
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchMetadata
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchSource
import com.silenteight.iris.bridge.scb.ingest.domain.model.Match
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationRequest
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationResponse

class Fixtures {

  static BatchMetadata BATCH_METADATA = new BatchMetadata(BatchSource.CBS)

  static def BATCH = Batch.builder()
      .id('batchId')
      .alertCount(3L)
      .metadata(BATCH_METADATA)
      .build()

  static def MATCH = Match.builder()
      .id('matchId')
      .build()

  static def matchId = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId.builder()
      .sourceId('matchId')
      .build()

  static def MATCH_ALERT = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match
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

  static def REGISTERED_ALERT_WITH_MATCHES = RegistrationResponse.RegisteredAlertWithMatches.builder()
      .alertId('alertId')
      .alertName('alertName')
      .alertStatus(AlertStatus.SUCCESS)
      .registeredMatches(
          [
              RegistrationResponse.RegisteredMatch.builder()
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
