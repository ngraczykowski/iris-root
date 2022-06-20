/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain

import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert
import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert.QcoMatchData
import com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.QcoRecommendationProperties

import spock.lang.Specification

class QcoRecommendationServiceSpec extends Specification {

  private static QCO_RECOMMENDATION = "qcoRecommendationAction"
  def qcoProperties = new QcoRecommendationProperties(true)
  def qcoRecommendationProvider = Mock(com
      .silenteight.iris.bridge.scb.outputrecommendation.infrastructure.QcoRecommendationProvider)
  def underTest = new QcoRecommendationService(qcoProperties, qcoRecommendationProvider)

  def "should run QCO process when qco is enabled and batch is from CBS"() {
    given:
    def recommendationsEvent = CBS_RECOMMENDATIONS_EVENT
    def numOfRecommendations = recommendationsEvent.recommendations().size()
    def qcoFacadeResponse = QCO_RECOMMENDATIONS_ALERT

    when:
    def result = underTest.process(recommendationsEvent)

    then:
    numOfRecommendations * qcoRecommendationProvider.process(_ as QcoRecommendationAlert) >> qcoFacadeResponse
    def recommendations = result.recommendations()
    def matches = recommendations.first().matches()
    with(matches.first()) {
      name() == Fixtures.MATCH_NAME
      recommendedComment() == Fixtures.MATCH_RECOMMENDED_COMMENT
      recommendedAction() == QCO_RECOMMENDATION
      qaSampled()
    }
  }

  def "should run QCO process when batch is not CBS"() {
    given:
    def recommendationsEvent = GNS_RT_RECOMMENDATIONS_EVENT
    def numOfRecommendations = recommendationsEvent.recommendations().size()
    def qcoFacadeResponse = QCO_RECOMMENDATIONS_ALERT

    when:
    def result = underTest.process(recommendationsEvent)

    then:
    numOfRecommendations * qcoRecommendationProvider.process(_ as QcoRecommendationAlert) >> qcoFacadeResponse
    def recommendations = result.recommendations()
    def matches = recommendations.first().matches()
    with(matches.first()) {
      name() == Fixtures.MATCH_NAME
      recommendedComment() == Fixtures.MATCH_RECOMMENDED_COMMENT
      recommendedAction() == QCO_RECOMMENDATION
      qaSampled()
    }
  }

  def "should return unchanged recommendations when QCO throws error"() {
    given:
    def recommendationsEvent = CBS_RECOMMENDATIONS_EVENT
    qcoRecommendationProvider.process(_ as QcoRecommendationAlert) >> {throw new RuntimeException()}

    when:
    def result = underTest.process(recommendationsEvent)

    then:
    result == recommendationsEvent
  }

  private com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent CBS_RECOMMENDATIONS_EVENT =
      com
          .silenteight
          .iris.bridge.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent.builder()
          .analysisName(Fixtures.ANALYSIS_NAME)
          .batchId(Fixtures.BATCH_ID)
          .batchMetadata(new com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.BatchMetadata(com
              .silenteight
              .iris
              .bridge
              .scb
              .outputrecommendation
              .domain
              .model
              .BatchSource.CBS))
          .recommendations(Fixtures.RECOMMENDATION_LIST)
          .build()

  private com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent GNS_RT_RECOMMENDATIONS_EVENT =
      com
          .silenteight
          .iris.bridge.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent.builder()
          .analysisName(Fixtures.ANALYSIS_NAME)
          .batchId(Fixtures.BATCH_ID)
          .batchMetadata(new com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.BatchMetadata(com
              .silenteight
              .iris
              .bridge
              .scb
              .outputrecommendation
              .domain
              .model
              .BatchSource.GNS_RT))
          .recommendations(Fixtures.RECOMMENDATION_LIST)
          .build()

  private QcoRecommendationAlert QCO_RECOMMENDATIONS_ALERT =
      QcoRecommendationAlert.builder()
          .alertId(Fixtures.ALERT_ID)
          .alertName(Fixtures.ALERT_NAME)
          .policyId(Fixtures.POLICY_ID)
          .matches(
              List.of(
                  QcoMatchData.builder()
                      .name(Fixtures.MATCH_NAME)
                      .stepId(Fixtures.MATCH_STEP_ID)
                      .comment(Fixtures.MATCH_RECOMMENDED_COMMENT)
                      .recommendation(QCO_RECOMMENDATION)
                      .qcoMarked(true)
                      .build()))
          .build()
}
