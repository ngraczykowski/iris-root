package com.silenteight.scb.outputrecommendation.adapter.outgoing

import com.silenteight.recommendation.api.library.v1.RecommendationsOut

import spock.lang.Specification

class RecommendationGrpcMapperSpec extends Specification {

  def 'should map RecommendationsOut to Recommendations'() {
    given:
    def recommendationsOut = RecommendationsOut.builder()
        .recommendations(GrpcAdapterFixtures.RECOMMENDATION_OUT_LIST)
        .statistics(GrpcAdapterFixtures.STATISTICS_OUT)
        .build()

    when:
    def result = RecommendationGrpcMapper.toResponse(recommendationsOut)

    then: 'assert recommendations'
    with(result) {
      recommendations().size() == 1
      with(recommendations().first()) {
        batchId() == GrpcAdapterFixtures.BATCH_ID
        policyId() == GrpcAdapterFixtures.POLICY_ID
        name() == GrpcAdapterFixtures.RECOMMENDATION_NAME
        recommendedAction() == GrpcAdapterFixtures.RECOMMENDED_ACTION
        recommendedComment() == GrpcAdapterFixtures.RECOMMENDATION_COMMENT
        recommendedAt() == GrpcAdapterFixtures.RECOMMENDED_AT
        with(alert()) {
          id() == GrpcAdapterFixtures.ALERT.id
          status().name() == GrpcAdapterFixtures.ALERT.status.name()
          metadata() == GrpcAdapterFixtures.ALERT.metadata
          errorMessage() == GrpcAdapterFixtures.ALERT.errorMessage
        }
        matches().size() == 1
        with(matches().first()) {
          id() == GrpcAdapterFixtures.MATCHES.first().id
          recommendedAction() == GrpcAdapterFixtures.MATCHES.first().recommendedAction
          recommendedComment() == GrpcAdapterFixtures.MATCHES.first().recommendationComment
          stepId() == GrpcAdapterFixtures.MATCHES.first().stepId
          fvSignature() == GrpcAdapterFixtures.MATCHES.first().fvSignature
          features() == GrpcAdapterFixtures.MATCHES.first().features
        }
      }

      and: 'assert statistics'
      with(result.statistics()) {
        totalProcessedCount() == GrpcAdapterFixtures.TOTAL_PROCESSED_COUNT
        totalUnableToProcessCount() == GrpcAdapterFixtures.TOTAL_UNABLE_TO_PROCESS_COUNT
        recommendedAlertsCount() == GrpcAdapterFixtures.RECOMMENDED_ALERT_COUNT
        with(recommendationsStatistics()) {
          truePositiveCount() == GrpcAdapterFixtures.TRUE_POSITIVE_COUNT
          falsePositiveCount() == GrpcAdapterFixtures.FALSE_POSITIVE_COUNT
          manualInvestigationCount() == GrpcAdapterFixtures.MANUAL_INVESTIGATION_COUNT
          errorCount() == GrpcAdapterFixtures.ERROR_COUNT
        }
      }
    }
  }
}
