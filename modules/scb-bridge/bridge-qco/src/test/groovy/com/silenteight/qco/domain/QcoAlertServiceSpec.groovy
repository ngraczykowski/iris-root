package com.silenteight.qco.domain

import com.silenteight.qco.domain.model.QcoRecommendationMatch

import spock.lang.Specification

class QcoAlertServiceSpec extends Specification {

  def matchProcessor = Mock(MatchProcessor)
  def underTest = new QcoAlertService(matchProcessor)

  def "should update match with qco recommendation"() {
    given:
    def qcoRecommendationAlert = Fixtures.QCO_RECOMMENDATION_ALERT
    matchProcessor.processMatch(_ as QcoRecommendationMatch) >> Fixtures.MATCH_SOLUTION

    when:
    def result = underTest.extractAndProcessRecommendationAlert(qcoRecommendationAlert)

    then:
    result.batchId() == Fixtures.BATCH_ID
    result.alertId() == Fixtures.ALERT_ID
    with(result.matches().first()){
      name() == Fixtures.MATCH_NAME
      stepId() == Fixtures.STEP_ID
      recommendation() == Fixtures.QCO_SOLUTION
      comment() == Fixtures.QCO_COMMENT
    }
  }
}
