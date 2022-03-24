package com.silenteight.scb.outputrecommendation.domain

import spock.lang.Specification
import spock.lang.Subject

class OutputRecommendationFacadeSpec extends Specification {

  def recommendationsProcessor = Mock(RecommendationsProcessor)

  @Subject
  def underTest = new OutputRecommendationFacade(recommendationsProcessor)

  def 'should invoke process batch completed'() {
    given:
    def command = PrepareRecommendationResponseCommand.builder().build()

    when:
    underTest.prepareCompletedBatchRecommendations(command)

    then:
    1 * recommendationsProcessor.processBatchCompleted(command)
  }

  def 'should invoke process batch error'() {
    given:
    def command = PrepareErrorRecommendationResponseCommand.builder().build()

    when:
    underTest.prepareErrorBatchRecommendations(command)

    then:
    1 * recommendationsProcessor.processBatchError(command)
  }
}
