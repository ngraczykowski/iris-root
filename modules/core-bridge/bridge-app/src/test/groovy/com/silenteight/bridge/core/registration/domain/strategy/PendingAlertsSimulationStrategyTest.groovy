package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.registration.domain.RegistrationFixtures
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository

import spock.lang.Specification
import spock.lang.Subject

class PendingAlertsSimulationStrategyTest extends Specification {

  def alertRepository = Mock(AlertRepository)

  @Subject
  def underTest = new PendingAlertsSimulationStrategy(alertRepository)

  def "should return #hasNoPendingAlerts when #desc"() {
    given:
    def batch = RegistrationFixtures.BATCH_BUILDER
        .alertsCount(alertsCount)
        .build()

    when:
    underTest.hasNoPendingAlerts(batch)

    then:
    alertRepository.countAllUdsFedAndErrorAlerts(batch.id()) >> completedAlertsCount

    where:
    alertsCount | completedAlertsCount | hasNoPendingAlerts | desc
    10          | 10                   | true               | "completedAlertsCount == alertsCount"
    10          | 1                    | false              | "completedAlertsCount < alertsCount"
  }
}
