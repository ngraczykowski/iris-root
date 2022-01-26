package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.Status

import spock.lang.Specification
import spock.lang.Subject

class RegistrationAlertResponseMapperSpec extends Specification {

  @Subject
  def underTest = new RegistrationAlertResponseMapper()

  def 'should map to registration alerts from alerts when alert status equals #alertStatus then registration alert status equals #registrationAlertStatus'() {
    given:
    def alerts = [
        Alert.builder()
            .name('alertName')
            .status(alertStatus)
            .alertId('alertId')
            .batchId('batchId')
            .metadata('metadata')
            .matches(
                [
                    Match.builder()
                        .name('matchName')
                        .status(Match.Status.REGISTERED)
                        .matchId('matchId')
                        .build()
                ]
            )
            .errorDescription('errorDescription')
            .build()
    ]

    when:
    def result = underTest.fromAlertsToRegistrationAlerts(alerts)

    then:
    with(result.first()) {
      id == 'alertId'
      name == 'alertName'
      status == registrationAlertStatus
      with(matches.first()) {
        id == 'matchId'
        name == 'matchName'
      }
    }

    where:
    alertStatus             | registrationAlertStatus
    AlertStatus.REGISTERED  | Status.SUCCESS
    AlertStatus.PROCESSING  | Status.SUCCESS
    AlertStatus.RECOMMENDED | Status.SUCCESS
    AlertStatus.ERROR       | Status.FAILURE
  }

  def 'should map to registration alerts from alerts with matches when alert status equals #alertStatus then registration alert status equals #registrationAlertStatus'() {
    given:
    def alerts = [
        AlertWithMatches.builder()
            .id('alertId')
            .name('alertName')
            .status(alertStatus)
            .metadata('metadata')
            .errorDescription('errorDescription')
            .matches(
                [
                    AlertWithMatches.Match.builder()
                        .id('matchId')
                        .name('matchName')
                        .build()
                ]
            )
            .build()
    ]

    when:
    def result = underTest.fromAlertsWithMatchesToRegistrationAlerts(alerts)

    then:
    with(result.first()) {
      id == 'alertId'
      name == 'alertName'
      status == registrationAlertStatus
      with(matches.first()) {
        id == 'matchId'
        name == 'matchName'
      }
    }

    where:
    alertStatus             | registrationAlertStatus
    AlertStatus.REGISTERED  | Status.SUCCESS
    AlertStatus.PROCESSING  | Status.SUCCESS
    AlertStatus.RECOMMENDED | Status.SUCCESS
    AlertStatus.ERROR       | Status.FAILURE
  }
}
