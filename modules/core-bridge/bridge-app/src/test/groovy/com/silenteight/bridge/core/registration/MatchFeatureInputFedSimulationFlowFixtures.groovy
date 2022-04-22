package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.proto.registration.api.v1.FedMatch
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus

class MatchFeatureInputFedSimulationFlowFixtures {

  static def EMPTY_STRING = ''

  static def ALERTS_COUNT = 2
  static def BATCH_ID = UUID.randomUUID().toString()
  static def ANALYSIS_NAME = UUID.randomUUID().toString()
  static def BATCH_METADATA = 'batchMetadata'
  static def BATCH_IS_SIMULATION = true
  static def BATCH_PRIORITY = 1

  static def FIRST_ALERT_NAME = UUID.randomUUID().toString()
  static def SECOND_ALERT_NAME = UUID.randomUUID().toString()
  static def ALERT_METADATA = 'alertMetadata'

  private static def createAlert(String alertName) {
    return Alert.builder()
        .name(alertName)
        .status(AlertStatus.REGISTERED)
        .alertId(UUID.randomUUID().toString())
        .batchId(BATCH_ID)
        .metadata(ALERT_METADATA)
        .matches([new Match(UUID.randomUUID().toString(), UUID.randomUUID().toString())])
        .errorDescription(EMPTY_STRING)
        .build()
  }

  private static def createMessageAlertMatchesFeatureInputFed(String alertName) {
    return MessageAlertMatchesFeatureInputFed.newBuilder()
        .setBatchId(BATCH_ID)
        .setAlertName(alertName)
        .setAlertErrorDescription(EMPTY_STRING)
        .setFeedingStatus(FeedingStatus.SUCCESS)
        .addAllFedMatches(
            [
                FedMatch.newBuilder()
                    .setMatchName(UUID.randomUUID().toString())
                    .build()
            ])
        .build()
  }

  static def BATCH = Batch.builder()
      .id(BATCH_ID)
      .analysisName(ANALYSIS_NAME)
      .policyName(UUID.randomUUID().toString())
      .alertsCount(ALERTS_COUNT)
      .batchMetadata(BATCH_METADATA)
      .status(BatchStatus.STORED)
      .batchPriority(BATCH_PRIORITY)
      .alertsCount(ALERTS_COUNT)
      .isSimulation(BATCH_IS_SIMULATION)
      .build()

  static def ALERTS = [
      createAlert(FIRST_ALERT_NAME),
      createAlert(SECOND_ALERT_NAME)
  ]

  static def ALERT_MATCHES_FEATURE_INPUT_FED_MESSAGES = [
      createMessageAlertMatchesFeatureInputFed(FIRST_ALERT_NAME),
      createMessageAlertMatchesFeatureInputFed(SECOND_ALERT_NAME)
  ]
}
