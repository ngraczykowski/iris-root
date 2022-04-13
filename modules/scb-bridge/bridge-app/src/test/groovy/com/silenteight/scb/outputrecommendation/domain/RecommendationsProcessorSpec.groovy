package com.silenteight.scb.outputrecommendation.domain

import com.silenteight.scb.ingest.domain.payload.PayloadConverter
import com.silenteight.scb.outputrecommendation.domain.model.*
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationApiClient
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationDeliveredEventPublisher
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationPublisher

import spock.lang.Specification
import spock.lang.Subject

class RecommendationsProcessorSpec extends Specification {

  def payloadConverter = Mock(PayloadConverter)
  def recommendationsMapper = new RecommendationsMapper(payloadConverter)
  def recommendationApiClient = Mock(RecommendationApiClient)
  def recommendationPublisher = Mock(RecommendationPublisher)
  def recommendationDeliveredEventPublisher = Mock(RecommendationDeliveredEventPublisher)
  def qcoRecommendationService = Mock(QcoRecommendationService)

  @Subject
  def underTest = new RecommendationsProcessor(
      qcoRecommendationService,
      recommendationsMapper,
      recommendationApiClient,
      recommendationPublisher,
      recommendationDeliveredEventPublisher
  )

  def setup() {
    payloadConverter.deserializeFromJsonToObject(Fixtures.BATCH_SERIALIZED_METADATA, BatchMetadata)
        >> new BatchMetadata(BatchSource.GNS_RT)
  }

  def 'should process batch completed'() {
    given:
    def command = Fixtures.PREPARE_RECOMMENDATION_RESPONSE_COMMAND

    when:
    underTest.processBatchCompleted(command)

    then:
    1 * recommendationApiClient.getRecommendations(Fixtures.ANALYSIS_NAME, []) >>
        Fixtures.RECOMMENDATIONS
    1 * qcoRecommendationService.process(_) >> {RecommendationsGeneratedEvent result -> result}
    1 * recommendationPublisher.publishCompleted(_) >> {RecommendationsGeneratedEvent event ->
      with(event) {
        batchId() == Fixtures.BATCH_ID
        analysisName() == Fixtures.ANALYSIS_NAME

        with(batchMetadata()) {
          batchSource() == Fixtures.BATCH_SOURCE
        }
        with(statistics()) {
          totalProcessedCount() == Fixtures.TOTAL_PROCESSED_COUNT
          totalUnableToProcessCount() == Fixtures.TOTAL_UNABLE_TO_PROCESS_COUNT
          recommendedAlertsCount() == Fixtures.RECOMMENDED_ALERTS_COUNT

          with(recommendationsStatistics()) {
            truePositiveCount() == Fixtures.TRUE_POSITIVE_COUNT
            falsePositiveCount() == Fixtures.FALSE_POSITIVE_COUNT
            manualInvestigationCount() == Fixtures.MANUAL_INVESTIGATION_COUNT
            errorCount() == Fixtures.ERROR_COUNT
          }
        }

        with(recommendations().first()) {
          batchId() == Fixtures.BATCH_ID
          name() == Fixtures.RECOMMENDATION_NAME
          recommendedAction() == Fixtures.RECOMMENDED_ACTION
          recommendedComment() == Fixtures.RECOMMENDED_COMMENT
          policyId() == Fixtures.POLICY_ID
          recommendedAt() == Fixtures.RECOMMENDED_AT

          with(alert()) {
            id() == Fixtures.ALERT_ID
            status() == Fixtures.ALERT_STATUS
            metadata() == Fixtures.ALERT_SERIALIZED_METADATA
            errorMessage() == Fixtures.ALERT_ERROR_MESSAGE
          }

          with(matches().first()) {
            id() == Fixtures.MATCH_ID
            recommendedAction() == Fixtures.MATCH_RECOMMENDED_ACTION
            recommendedComment() == Fixtures.MATCH_RECOMMENDED_COMMENT
            stepId() == Fixtures.MATCH_STEP_ID
            fvSignature() == Fixtures.MATCH_FV_SIGNATURE
            features() == Fixtures.FEATURES
          }
        }
      }
    }

    1 * recommendationDeliveredEventPublisher.publish(_) >> {RecommendationsDeliveredEvent event ->
      with(event) {
        batchId() == Fixtures.BATCH_ID
      }
    }
  }

  def 'should process batch error'() {
    given:
    def command = Fixtures.PREPARE_ERROR_RECOMMENDATION_RESPONSE_COMMAND

    when:
    underTest.processBatchError(command)

    then:
    1 * recommendationPublisher.publishError(_) >> {ErrorRecommendationsGeneratedEvent event ->
      with(event) {
        batchId() == Fixtures.BATCH_ID
        errorDescription() == Fixtures.ERROR_DESCRIPTION
        with(batchMetadata()) {
          batchSource() == Fixtures.BATCH_SOURCE
        }
        with(statistics()) {
          totalProcessedCount() == 0
          totalUnableToProcessCount() == 0
          recommendedAlertsCount() == 0

          with(recommendationsStatistics()) {
            truePositiveCount() == 0
            falsePositiveCount() == 0
            manualInvestigationCount() == 0
            errorCount() == 0
          }
        }
      }
    }
    1 * recommendationDeliveredEventPublisher.publish(_) >> {RecommendationsDeliveredEvent event ->
      with(event) {
        batchId() == Fixtures.BATCH_ID
      }
    }
  }
}
