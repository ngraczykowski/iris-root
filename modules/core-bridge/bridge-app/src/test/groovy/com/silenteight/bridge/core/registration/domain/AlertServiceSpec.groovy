package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertStatus
import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.Match
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertId
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRegistrationService
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository

import spock.lang.Specification
import spock.lang.Subject

class AlertServiceSpec extends Specification {

  def mapper = Mock(AlertMapper)
  def alertRepository = Mock(AlertRepository)
  def alertRegistrationService = Mock(AlertRegistrationService)

  @Subject
  def underTest = new AlertService(mapper, alertRepository, alertRegistrationService)

  def 'should register and save alerts with matches'() {
    given:
    def alertsWithMatches = [
        AlertWithMatches.builder()
            .alertId('alert_id_1')
            .matches([new Match('match_id_11')])
            .alertStatus(AlertStatus.SUCCESS)
            .build(),
        AlertWithMatches.builder()
            .alertId('alert_id_2')
            .matches([new Match('match_id_21')])
            .alertStatus(AlertStatus.SUCCESS)
            .build()
    ]

    def command = new RegisterAlertsCommand('batch_id_1', alertsWithMatches)

    def alertsToRegister = new AlertsToRegister(
        [new AlertsToRegister.AlertWithMatches(
            'alert_id_1',
            [new AlertsToRegister.Match('match_id_11')]),
         new AlertsToRegister.AlertWithMatches(
             'alert_id_2',
             [new AlertsToRegister.Match('match_id_21')]),
        ])

    def registeredAlerts = new RegisteredAlerts(
        [RegisteredAlerts.AlertWithMatches.builder().build(), RegisteredAlerts.AlertWithMatches.builder().build()])

    def alerts = [Alert.builder().build(), Alert.builder().build()]

    when:
    underTest.registerAlertsAndMatches(command)

    then:
    1 * alertRepository.findAllAlertIdsByBatchIdAndAlertIdIn(_ as String, _ as List<String>) >> []
    1 * mapper.toAlertsToRegister(_ as List<AlertWithMatches>) >> alertsToRegister
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts
    1 * mapper.toAlerts(_ as RegisteredAlerts, 'batch_id_1') >> alerts
    1 * alertRepository.saveAlerts(_ as List<Alert>)
    1 * mapper.toErrorAlerts([] as List<AlertWithMatches>, 'batch_id_1') >> []
  }

  def 'should register and save only alerts not existing in DB'() {
    given:
    def alertIdExistingInDb = 'alert_id_1'

    def alert1 = AlertWithMatches.builder()
        .alertId(alertIdExistingInDb)
        .matches([new Match('match_id_11')])
        .alertStatus(AlertStatus.SUCCESS)
        .build()

    def alert2 = AlertWithMatches.builder()
        .alertId('alert_id_2')
        .matches([new Match('match_id_21')])
        .alertStatus(AlertStatus.SUCCESS)
        .build()

    def command = new RegisterAlertsCommand('batch_id_1', [alert1, alert2])

    def alertsExistingInDb = [new AlertId(alertIdExistingInDb)]

    def alertsToRegister = new AlertsToRegister(
        [new AlertsToRegister.AlertWithMatches(
            'alert_id_1',
            [new AlertsToRegister.Match('match_id_11')]),
         new AlertsToRegister.AlertWithMatches(
             'alert_id_2',
             [new AlertsToRegister.Match('match_id_21')]),
        ])

    def registeredAlerts = new RegisteredAlerts(
        [RegisteredAlerts.AlertWithMatches.builder().build(), RegisteredAlerts.AlertWithMatches.builder().build()])

    def alerts = [Alert.builder().alertId('alert_id_2').build()]

    when:
    underTest.registerAlertsAndMatches(command)

    then:
    1 * alertRepository.findAllAlertIdsByBatchIdAndAlertIdIn(_ as String, _ as List<String>) >> alertsExistingInDb
    1 * mapper.toAlertsToRegister([alert2]) >> alertsToRegister
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts
    1 * mapper.toAlerts(_ as RegisteredAlerts, 'batch_id_1') >> alerts
    1 * alertRepository.saveAlerts(_ as List<Alert>)
    1 * mapper.toErrorAlerts(_ as List<AlertWithMatches>, 'batch_id_1') >> []
  }

  def 'should register alerts with the status SUCCESS and create succeeded and failed in DB'() {
    given:
    def succeededAlert = AlertWithMatches.builder()
        .alertId('alert_id_2')
        .matches([new Match('match_id_21')])
        .alertStatus(AlertStatus.SUCCESS)
        .build()

    def failedAlert = AlertWithMatches.builder()
        .alertId('alert_id_1')
        .matches([new Match('match_id_11')])
        .alertStatus(AlertStatus.FAILURE)
        .build()

    def command = new RegisterAlertsCommand('batch_id_1', [succeededAlert, failedAlert])

    def alertsToRegister = new AlertsToRegister(
        [new AlertsToRegister.AlertWithMatches(
            'alert_id_1',
            [new AlertsToRegister.Match('match_id_11')]),
         new AlertsToRegister.AlertWithMatches(
             'alert_id_2',
             [new AlertsToRegister.Match('match_id_21')]),
        ])

    def registeredAlerts = new RegisteredAlerts(
        [RegisteredAlerts.AlertWithMatches.builder().build(), RegisteredAlerts.AlertWithMatches.builder().build()])

    when:
    underTest.registerAlertsAndMatches(command)

    then:
    1 * alertRepository.findAllAlertIdsByBatchIdAndAlertIdIn(_ as String, _ as List<String>) >> []
    1 * mapper.toAlertsToRegister(_ as List<AlertWithMatches>) >> alertsToRegister
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts
    1 * mapper.toAlerts(_ as RegisteredAlerts, 'batch_id_1') >> [Alert.builder().build()]
    1 * alertRepository.saveAlerts(_ as List<Alert>)
    1 * mapper.toErrorAlerts(_ as List<AlertWithMatches>, 'batch_id_1') >> [Alert.builder().build()]
    1 * alertRepository.saveAlerts(_ as List<Alert>)
  }
}
