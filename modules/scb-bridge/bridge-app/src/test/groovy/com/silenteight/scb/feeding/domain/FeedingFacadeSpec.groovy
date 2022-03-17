package com.silenteight.scb.feeding.domain

import com.silenteight.scb.feeding.domain.model.FeedUdsCommand
import com.silenteight.scb.feeding.domain.port.outgoing.FeedingEventPublisher
import com.silenteight.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient
import com.silenteight.scb.feeding.fixtures.Fixtures

import spock.lang.Specification
import spock.lang.Subject

class FeedingFacadeSpec extends Specification {

  def feedingService = Mock(FeedingService)
  def universalDatasourceApiClient = Mock(UniversalDatasourceApiClient)
  def feedingEventPublisher = Mock(FeedingEventPublisher)

  @Subject
  def underTest = new FeedingFacade(
      feedingService,
      universalDatasourceApiClient,
      feedingEventPublisher
  )

  def "should feed uds with learning alert"() {
    given:
    def feedUdsCommand = new FeedUdsCommand(Fixtures.LEARNING_ALERT)

    when:
    underTest.feedUds(feedUdsCommand)

    then:
    1 * feedingService
        .createAgentInputIns(feedUdsCommand.alert(), feedUdsCommand.alert().matches().get(0))
    0 * feedingEventPublisher.publish(_)
  }

  def "should feed uds with recommendation alert"() {
    given:
    def feedUdsCommand = new FeedUdsCommand(Fixtures.RECOMMENDATION_ALERT)

    when:
    underTest.feedUds(feedUdsCommand)

    then:
    1 * feedingService
        .createAgentInputIns(feedUdsCommand.alert(), feedUdsCommand.alert().matches().get(0))

    1 * feedingEventPublisher.publish(_)
  }
}
