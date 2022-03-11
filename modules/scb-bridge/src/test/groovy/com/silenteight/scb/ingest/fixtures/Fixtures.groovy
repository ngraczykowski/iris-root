package com.silenteight.scb.ingest.fixtures

import com.silenteight.scb.ingest.domain.model.AlertMetadata
import com.silenteight.scb.ingest.domain.model.AlertStatus
import com.silenteight.scb.ingest.domain.model.AlertWithMatchesMetadata
import com.silenteight.scb.ingest.domain.model.Batch
import com.silenteight.scb.ingest.domain.model.BatchMetadata
import com.silenteight.scb.ingest.domain.model.Match
import com.silenteight.scb.ingest.domain.model.RegistrationRequest
import com.silenteight.scb.ingest.domain.model.RegistrationResponse
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

  static def ALERT_WITH_MATCHES_METADATA = AlertWithMatchesMetadata.builder()
      .alertId('alertId')
      .status(AlertStatus.SUCCESS)
      .metadata(ALERT_METADATA)
      .matches([MATCH])
      .build()

  static AlertMetadata ALERT_METADATA = AlertMetadata.builder()
      .currentVersionId("someCurrentVersionId")
      .stopDescriptorNames(["firstStopDescriptorName", "secondStopDescriptorName"])
      .datasetId("someDatasetId")
      .datasetName("someDatasetBame")
      .uniqueCustId("someUniqueCustId")
      .masterId("someMasterId")
      .busDate("someBusDate")
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
      .of(BATCH, [ALERT_WITH_MATCHES_METADATA, ALERT_WITH_MATCHES_METADATA])

  static REGISTRATION_RESPONSE = RegistrationResponse.builder()
      .registeredAlertWithMatches([REGISTERED_ALERT_WITH_MATCHES, REGISTERED_ALERT_WITH_MATCHES])
      .build()
}
