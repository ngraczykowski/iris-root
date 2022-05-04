package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.proto.registration.api.v1.FedMatch
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus

class UdsFedFixtures {

  static def ANALYSIS_NAME = UUID.randomUUID().toString()
  static def BATCH_METADATA = 'batchMetadata'
  static def BATCH_PRIORITY = 1

  static def ALERT_METADATA = 'alertMetadata'
  static def ALERT_ERROR_DESCRIPTION = 'Failed to flatten alert payload.'

  static def createAlert(String batchId, String alertName, AlertStatus status) {
    Alert.builder()
        .name(alertName)
        .status(status)
        .alertId(UUID.randomUUID().toString())
        .batchId(batchId)
        .metadata(ALERT_METADATA)
        .matches([new Match(UUID.randomUUID().toString(), UUID.randomUUID().toString())])
        .build()
  }

  static def createMessage(String batchId, String alertName, FeedingStatus feedingStatus) {
    def errorDescription = (feedingStatus == FeedingStatus.FAILURE) ? ALERT_ERROR_DESCRIPTION : ''
    MessageAlertMatchesFeatureInputFed.newBuilder()
        .setBatchId(batchId)
        .setAlertName(alertName)
        .setAlertErrorDescription(errorDescription)
        .setFeedingStatus(feedingStatus)
        .addAllFedMatches(
            [
                FedMatch.newBuilder()
                    .setMatchName(UUID.randomUUID().toString())
                    .build()
            ])
        .build()
  }

  static def createBatch(boolean isSimulation, Long alertsCount) {
    def analysisName = isSimulation ? '' : ANALYSIS_NAME
    Batch.builder()
        .id(UUID.randomUUID().toString())
        .analysisName(analysisName)
        .policyName(UUID.randomUUID().toString())
        .batchMetadata(BATCH_METADATA)
        .status(BatchStatus.STORED)
        .batchPriority(BATCH_PRIORITY)
        .alertsCount(alertsCount)
        .isSimulation(isSimulation)
        .build()
  }
}
