package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.command.ProceedDataRetentionOnRecommendationsCommand
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository

import spock.lang.Specification
import spock.lang.Subject

class RecommendationsDataRetentionServiceSpec extends Specification {

  def recommendationRepository = Mock(RecommendationRepository)

  @Subject
  def underTest = new RecommendationsDataRetentionService(recommendationRepository)

  def 'should not call repository when there are no alert names'() {
    when:
    underTest.performDataRetention(new ProceedDataRetentionOnRecommendationsCommand([]))

    then:
    0 * recommendationRepository._
  }

  def 'should call repository'() {
    given:
    def alertNames = ['alert1']

    when:
    underTest.performDataRetention(new ProceedDataRetentionOnRecommendationsCommand(alertNames))

    then:
    1 * recommendationRepository.clearCommentAndPayload(alertNames)
  }
}
