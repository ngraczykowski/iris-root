package com.silenteight.scb.feeding.domain

import com.silenteight.scb.feeding.domain.model.FeedUdsCommand
import com.silenteight.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient
import com.silenteight.scb.feeding.fixtures.Fixtures

import spock.lang.Specification
import spock.lang.Subject

class FeedingFacadeSpec extends Specification {

  def feedingService = Mock(FeedingService)
  def universalDatasourceApiClient = Mock(UniversalDatasourceApiClient)

  @Subject
  def underTest = new FeedingFacade(
      feedingService,
      universalDatasourceApiClient
  )

  def "should feed uds"() {
    given:
    def feedUdsCommand = new FeedUdsCommand(Fixtures.ALERT, List.of(Fixtures.MATCH))

    when:
    underTest.feedUds(feedUdsCommand)

    then:
    1 * feedingService.createAgentInputIns(feedUdsCommand.alert(), feedUdsCommand.matches().get(0))
  }
}
