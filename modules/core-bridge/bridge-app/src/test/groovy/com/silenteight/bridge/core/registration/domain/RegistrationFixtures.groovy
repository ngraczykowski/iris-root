package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.recommendation.domain.FixturesMatchMetaData
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand.FedMatch
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand.FeedingStatus
import com.silenteight.bridge.core.registration.domain.command.GetAlertsWithMatchesCommand
import com.silenteight.bridge.core.registration.domain.command.NotifyBatchErrorCommand
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.command.GetBatchWithAlertsCommand
import com.silenteight.bridge.core.registration.domain.command.RegisterBatchCommand
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.BatchError
import com.silenteight.bridge.core.registration.domain.model.BatchId
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy

class RegistrationFixtures {

  static def ANALYSIS_NAME = "analysisName"
  static def POLICY_NAME = FixturesMatchMetaData.REASON_POLICY
  static def ALERTS_COUNT = 25
  static def METADATA = "batchMetadata"
  static def BATCH_PRIORITY = 1
  static def ERROR_DESCRIPTION = "error occurred"

  static def ALERT_WITH_MATCHES = [AlertWithMatches.builder().build()]

  private static def BATCH_BUILDER =
      Batch.builder()
          .id(Fixtures.BATCH_ID)
          .analysisName(ANALYSIS_NAME)
          .policyName(POLICY_NAME)
          .alertsCount(ALERTS_COUNT)
          .batchMetadata(METADATA)

  static def BATCH = BATCH_BUILDER
      .build()

  static Batch batch(BatchStatus status) {
    return BATCH_BUILDER.status(status).build()
  }

  static def BATCH_ID_PROJECTION = BatchId.from(BATCH)
  static def BATCH_ID_WITH_POLICY_PROJECTION = new BatchIdWithPolicy(Fixtures.BATCH_ID, POLICY_NAME)

  static def REGISTER_BATCH_COMMAND = new RegisterBatchCommand(
      Fixtures.BATCH_ID, ALERTS_COUNT, METADATA, BATCH_PRIORITY)

  static def REGISTER_ALERTS_COMMAND = new RegisterAlertsCommand(
      Fixtures.BATCH_ID, ALERT_WITH_MATCHES)

  static def ADD_ALERT_TO_ANALYSIS_COMMAND = AddAlertToAnalysisCommand.builder()
      .batchId(Fixtures.BATCH_ID)
      .alertName('alertName')
      .feedingStatus(FeedingStatus.SUCCESS)
      .fedMatches([new FedMatch('matchName')])
      .build()

  static def NOTIFY_BATCH_ERROR_COMMAND = new NotifyBatchErrorCommand(
      Fixtures.BATCH_ID, ERROR_DESCRIPTION, METADATA)

  static def GET_BATCH_WITH_ALERTS_COMMAND = new GetBatchWithAlertsCommand(ANALYSIS_NAME)

  static def GET_ALERTS_WITH_MATCHES_COMMAND = new GetAlertsWithMatchesCommand(Fixtures.BATCH_ID)

  static def BATCH_ERROR = new BatchError(Fixtures.BATCH_ID, METADATA, ERROR_DESCRIPTION)
}
