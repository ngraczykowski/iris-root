package com.silenteight.scb.outputrecommendation.domain

import com.silenteight.qco.domain.model.QcoRecommendationAlert
import com.silenteight.qco.domain.model.QcoRecommendationAlert.QcoMatchData
import com.silenteight.scb.outputrecommendation.domain.model.BatchMetadata
import com.silenteight.scb.outputrecommendation.domain.model.BatchSource
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent
import com.silenteight.scb.outputrecommendation.infrastructure.QcoRecommendationProperties
import com.silenteight.scb.outputrecommendation.infrastructure.QcoRecommendationProvider

import spock.lang.Specification

class QcoRecommendationServiceSpec extends Specification {

  private static QCO_RECOMMENDATION = "qcoRecommendationAction"
  def qcoProperties = new QcoRecommendationProperties(true)
  def qcoRecommendationProvider = Mock(QcoRecommendationProvider)
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
    }
  }

  def "should not run QCO process when batch is not CBS"() {
    given:
    def recommendationsEvent = GNS_RT_RECOMMENDATIONS_EVENT

    when:
    def result = underTest.process(recommendationsEvent)

    then:
    0 * qcoRecommendationProvider.process(_ as QcoRecommendationAlert)
    result == recommendationsEvent
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

  private RecommendationsGeneratedEvent CBS_RECOMMENDATIONS_EVENT =
      RecommendationsGeneratedEvent.builder()
          .analysisName(Fixtures.ANALYSIS_NAME)
          .batchId(Fixtures.BATCH_ID)
          .batchMetadata(new BatchMetadata(BatchSource.CBS))
          .recommendations(Fixtures.RECOMMENDATION_LIST)
          .build()

  private RecommendationsGeneratedEvent GNS_RT_RECOMMENDATIONS_EVENT =
      RecommendationsGeneratedEvent.builder()
          .analysisName(Fixtures.ANALYSIS_NAME)
          .batchId(Fixtures.BATCH_ID)
          .batchMetadata(new BatchMetadata(BatchSource.GNS_RT))
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
                      .build()))
          .build()
}
