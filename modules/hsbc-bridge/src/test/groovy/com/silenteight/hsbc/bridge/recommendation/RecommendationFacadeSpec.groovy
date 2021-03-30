package com.silenteight.hsbc.bridge.recommendation

import spock.lang.Specification

class RecommendationFacadeSpec extends Specification {

  def repository = Mock(RecommendationRepository)
  def underTest = new RecommendationFacade(repository)

  def 'should save recommendation'() {
    given:
    def command = StoreRecommendationCommand.builder().build()

    when:
    def result = underTest.storeRecommendation(command)

    then:
    result == 2
    1 * repository.save(_ as RecommendationEntity) >> {RecommendationEntity entity -> entity.id = 2}
  }
}
