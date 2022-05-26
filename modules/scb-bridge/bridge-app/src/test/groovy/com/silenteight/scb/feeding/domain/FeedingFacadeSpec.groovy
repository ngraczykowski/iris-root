package com.silenteight.scb.feeding.domain

import com.silenteight.scb.feeding.domain.agent.input.AgentInputFactory
import com.silenteight.scb.feeding.domain.port.outgoing.FeedingEventPublisher
import com.silenteight.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient
import com.silenteight.scb.feeding.fixtures.Fixtures
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext

import spock.lang.Specification
import spock.lang.Subject

class FeedingFacadeSpec extends Specification {

  def feedingService = Mock(AgentInputFactory)
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
    def alert = Fixtures.LEARNING_ALERT

    when:
    underTest.feedUds(alert, RegistrationBatchContext.LEARNING_CONTEXT)

    then:
    1 * feedingService.createAgentInputIns(alert, alert.matches().get(0))
    0 * feedingEventPublisher.publish(_)
  }

  def "should feed uds with recommendation alert"() {
    given:
    def alert = Fixtures.RECOMMENDATION_ALERT

    when:
    underTest.feedUds(alert, RegistrationBatchContext.CBS_CONTEXT)

    then:
    1 * feedingService.createAgentInputIns(alert, alert.matches().get(0))

    1 * feedingEventPublisher.publish(_)
  }
}
