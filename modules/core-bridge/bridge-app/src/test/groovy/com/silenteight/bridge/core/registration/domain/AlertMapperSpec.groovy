package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertStatus
import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.Alert.Status
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts

import spock.lang.Specification
import spock.lang.Subject

class AlertMapperSpec extends Specification {

  @Subject
  def underTest = new AlertMapper()

  def 'should map command alerts to register alerts'() {
    given:
    def matchesIn = [new RegisterAlertsCommand.Match('matchId')]
    def alerts = [
        AlertWithMatches.builder()
            .alertId('alertId')
            .errorDescription('')
            .matches(matchesIn)
            .alertStatus(AlertStatus.SUCCESS)
            .build()
    ]

    when:
    def result = underTest.toAlertsToRegister(alerts)

    then:
    with(result.registerAlertsWithMatches().first()) {
      alertId() == 'alertId'
      matches().first().matchId() == 'matchId'
    }
  }

  def 'should map registered alerts to succeeded alerts'() {
    given:
    def batchIdIn = 'batchId'
    def registeredMatches = [
        new RegisteredAlerts.Match('matchId', 'matchName')
    ]

    def registeredAlertsWithMatches = [
        RegisteredAlerts.AlertWithMatches.builder()
            .alertId('alertId')
            .name('alertName')
            .matches(registeredMatches)
            .build()
    ]
    def registeredAlerts = new RegisteredAlerts(registeredAlertsWithMatches)

    when:
    def result = underTest.toAlerts(registeredAlerts, batchIdIn)

    then:
    with(result.first()) {
      name() == 'alertName'
      status() == Status.REGISTERED
      alertId() == 'alertId'
      batchId() == 'batchId'
      errorDescription() == null
      with(matches().first()) {
        name() == 'matchName'
        status() == Match.Status.REGISTERED
        matchId() == 'matchId'
      }
    }
  }

  def 'should map command alerts to error alerts'() {
    given:
    def batchIdIn = 'batchId'
    def commandMatches = [new RegisterAlertsCommand.Match('matchId')]
    def failedAlerts = [
        AlertWithMatches.builder()
            .alertId('alertId')
            .errorDescription('someErrorDescription')
            .matches(commandMatches)
            .alertStatus(AlertStatus.FAILURE)
            .build()
    ]

    when:
    def result = underTest.toErrorAlerts(failedAlerts, batchIdIn)

    then:
    with(result.first()) {
      name() == null
      status() == Status.ERROR
      alertId() == 'alertId'
      batchId() == 'batchId'
      errorDescription() == 'someErrorDescription'
      with(matches().first()) {
        name() == null
        status() == Match.Status.ERROR
        matchId() == 'matchId'
      }
    }
  }
}
