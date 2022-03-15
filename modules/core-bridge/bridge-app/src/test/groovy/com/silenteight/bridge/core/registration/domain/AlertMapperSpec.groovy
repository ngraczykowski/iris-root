package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.Match
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts

import spock.lang.Specification
import spock.lang.Subject

class AlertMapperSpec extends Specification {

  @Subject
  def underTest = new AlertMapper()

  def 'should map command alerts to AlertsToRegister'() {
    given:
    def priorityIn = 0;
    def matchesIn = [
        new Match('matchId')
    ]
    def alerts = [
        AlertWithMatches.builder()
            .alertId('alertId')
            .errorDescription('')
            .matches(matchesIn)
            .alertStatus(RegisterAlertsCommand.AlertStatus.SUCCESS)
            .build()
    ]

    when:
    def result = underTest.toAlertsToRegister(alerts, priorityIn)

    then:
    with(result.registerAlertsWithMatches().first()) {
      alertId() == 'alertId'
      priority() == priorityIn
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
    def successAlerts = [
        AlertWithMatches.builder()
            .alertId('alertId')
            .alertMetadata('alertMetadata')
            .build()
    ]

    when:
    def result = underTest.toAlerts(registeredAlerts, successAlerts, batchIdIn)

    then:
    with(result.first()) {
      name() == 'alertName'
      status() == AlertStatus.REGISTERED
      alertId() == 'alertId'
      batchId() == 'batchId'
      errorDescription() == null
      metadata() == 'alertMetadata'
      with(matches().first()) {
        name() == 'matchName'
        matchId() == 'matchId'
      }
    }
  }

  def 'should map command alerts to error alerts'() {
    given:
    def batchIdIn = 'batchId'
    def commandMatches = [
        new Match('matchId')
    ]
    def failedAlerts = [
        AlertWithMatches.builder()
            .alertId('alertId')
            .errorDescription('someErrorDescription')
            .matches(commandMatches)
            .alertStatus(RegisterAlertsCommand.AlertStatus.FAILURE)
            .build()
    ]

    when:
    def result = underTest.toErrorAlerts(failedAlerts, batchIdIn)

    then:
    with(result.first()) {
      name() == null
      status() == AlertStatus.ERROR
      alertId() == 'alertId'
      batchId() == 'batchId'
      errorDescription() == 'someErrorDescription'
      with(matches().first()) {
        name() == null
        matchId() == 'matchId'
      }
    }
  }
}
