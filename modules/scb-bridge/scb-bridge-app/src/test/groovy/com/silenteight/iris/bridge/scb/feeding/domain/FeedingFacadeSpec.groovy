/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain

import spock.lang.Specification
import spock.lang.Subject

class FeedingFacadeSpec extends Specification {

  def feedingService = Mock(com
      .silenteight.iris.bridge.scb.feeding.domain.agent.input.AgentInputFactory)
  def universalDatasourceApiClient = Mock(com
      .silenteight.iris.bridge.scb.feeding.domain.port.outgoing.UniversalDatasourceApiClient)
  def feedingEventPublisher = Mock(com
      .silenteight.iris.bridge.scb.feeding.domain.port.outgoing.FeedingEventPublisher)

  @Subject
  def underTest = new FeedingFacade(
      feedingService,
      universalDatasourceApiClient,
      feedingEventPublisher
  )

  def "should feed uds with learning alert"() {
    given:
    def alert = com.silenteight.iris.bridge.scb.feeding.fixtures.Fixtures.LEARNING_ALERT

    when:
    underTest.feedUds(alert, com
        .silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext.LEARNING_CONTEXT)

    then:
    1 * feedingService.createAgentInputIns(alert, alert.matches().get(0))
    0 * feedingEventPublisher.publish(_)
  }

  def "should feed uds with recommendation alert"() {
    given:
    def alert = com.silenteight.iris.bridge.scb.feeding.fixtures.Fixtures.RECOMMENDATION_ALERT

    when:
    underTest.feedUds(alert, com
        .silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext.CBS_CONTEXT)

    then:
    1 * feedingService.createAgentInputIns(alert, alert.matches().get(0))

    1 * feedingEventPublisher.publish(_)
  }
}
