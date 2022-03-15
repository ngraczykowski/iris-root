package com.silenteight.bridge.core.registration.adapter.outgoing

import com.silenteight.adjudication.api.library.v1.alert.AlertMatchOut
import com.silenteight.adjudication.api.library.v1.alert.AlertWithMatchesOut
import com.silenteight.adjudication.api.library.v1.alert.RegisterAlertsAndMatchesOut
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister.Match

import spock.lang.Specification
import spock.lang.Subject

class AlertRegistrationMapperSpec extends Specification {

  @Subject
  def underTest = new AlertRegistrationMapper()

  def 'should map RegisterAlerts to RegisterAlertsAndMatchesIn'() {
    given:
    def alertId = 'alertId'
    def priority = 0
    def matches = [new Match('matchId')]
    def registerAlertWithMatches = [new AlertWithMatches(alertId, priority, matches)]

    def registerAlerts = new AlertsToRegister(registerAlertWithMatches)

    when:
    def result = underTest.toRequest(registerAlerts)

    then:
    with(result.getAlertsWithMatches().first()) {
      getAlertId() == 'alertId'
      getAlertPriority() == 0
      getMatchIds().first() == 'matchId'
    }
  }

  def 'should map RegisterAlertsAndMatchesOut to RegisteredAlerts'() {
    given:
    def matchesIn = [
        AlertMatchOut.builder()
            .matchId('matchId')
            .name('matchName')
            .build()
    ]

    def alertsWithMatchesOut = [
        AlertWithMatchesOut.builder()
            .alertId('alertId')
            .alertName('alertName')
            .matches(matchesIn)
            .build()
    ]

    def registerAlertsAndMatchesOut = RegisterAlertsAndMatchesOut.builder()
        .alertWithMatches(alertsWithMatchesOut)
        .build()

    when:
    def result = underTest.toRegisteredAlerts(registerAlertsAndMatchesOut)

    then:
    with(result.registeredAlertsWithMatches().first()) {
      alertId() == 'alertId'
      name() == 'alertName'
      with(matches().first()) {
        matchId() == 'matchId'
        name() == 'matchName'
      }
    }
  }
}
