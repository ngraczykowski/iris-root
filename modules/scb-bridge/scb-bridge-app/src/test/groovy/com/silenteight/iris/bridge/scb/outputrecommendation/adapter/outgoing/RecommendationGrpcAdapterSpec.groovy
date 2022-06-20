/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing

import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient

import spock.lang.Specification
import spock.lang.Subject

class RecommendationGrpcAdapterSpec extends Specification {

  def recommendationServiceClient = Mock(RecommendationServiceClient)

  @Subject
  def underTest = new RecommendationGrpcAdapter(recommendationServiceClient)

  def 'should get recommendations'() {
    given:
    def analysisName = GrpcAdapterFixtures.ANALYSIS_NAME
    def alertIds = GrpcAdapterFixtures.ALERT_IDS

    and:
    1 * recommendationServiceClient.getRecommendations(GrpcAdapterFixtures.RECOMMENDATIONS_IN) >>
        GrpcAdapterFixtures.RECOMMENDATIONS_OUT

    when:
    def result = underTest.getRecommendations(analysisName, alertIds)

    then:
    with(result.recommendations().first()) {
      batchId() == GrpcAdapterFixtures.BATCH_ID
      name() == GrpcAdapterFixtures.RECOMMENDATION_NAME
      recommendedAction() == GrpcAdapterFixtures.RECOMMENDED_ACTION
      recommendedComment() == GrpcAdapterFixtures.RECOMMENDATION_COMMENT
      policyId() == GrpcAdapterFixtures.POLICY_ID
      recommendedAt() == GrpcAdapterFixtures.RECOMMENDED_AT

      with(alert()) {
        id() == GrpcAdapterFixtures.ALERT_ID
        name() == GrpcAdapterFixtures.ALERT_NAME
        status().name() == GrpcAdapterFixtures.ALERT_STATUS.name()
        metadata() == GrpcAdapterFixtures.ALERT_METADATA
        errorMessage() == GrpcAdapterFixtures.ERROR_MESSAGE
      }

      with(matches().first()) {
        id() == GrpcAdapterFixtures.MATCH_ID
        name() == GrpcAdapterFixtures.MATCH_NAME
        recommendedAction() == GrpcAdapterFixtures.MATCH_RECOMMENDED_ACTION
        recommendedComment() == GrpcAdapterFixtures.MATCH_RECOMMENDATION_COMMENT
        stepId() == GrpcAdapterFixtures.STEP_ID
        fvSignature() == GrpcAdapterFixtures.FV_SIGNATURE
        features().get(GrpcAdapterFixtures.FEATURE_KEY) == GrpcAdapterFixtures.FEATURE_VALUE
      }
    }
  }
}
