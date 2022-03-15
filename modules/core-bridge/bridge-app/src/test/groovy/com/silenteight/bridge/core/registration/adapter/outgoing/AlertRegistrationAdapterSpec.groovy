package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.adjudication.api.library.v1.alert.AlertServiceClient
import com.silenteight.adjudication.api.library.v1.alert.RegisterAlertsAndMatchesIn
import com.silenteight.adjudication.api.library.v1.alert.RegisterAlertsAndMatchesOut
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister.Match

import spock.lang.Specification
import spock.lang.Subject

class AlertRegistrationAdapterSpec extends Specification {

  def mapper = Mock(AlertRegistrationMapper)
  def alertServiceClient = Mock(AlertServiceClient)

  @Subject
  def underTest = new AlertRegistrationAdapter(mapper, alertServiceClient)

  def 'should register alerts and matches'() {
    given:
    def alertId = 'alertId'
    def priority = 0
    def matches = [new Match('matchId')]
    def registerAlerts = new AlertsToRegister([new AlertWithMatches(alertId, priority, matches)])

    when:
    underTest.registerAlerts(registerAlerts)

    then:
    1 * mapper.toRequest(_ as AlertsToRegister) >> RegisterAlertsAndMatchesIn.builder().build()
    1 * alertServiceClient.registerAlertsAndMatches(_ as RegisterAlertsAndMatchesIn) >>
        RegisterAlertsAndMatchesOut.builder().build()
    1 * mapper.toRegisteredAlerts(_ as RegisterAlertsAndMatchesOut)
  }
}
