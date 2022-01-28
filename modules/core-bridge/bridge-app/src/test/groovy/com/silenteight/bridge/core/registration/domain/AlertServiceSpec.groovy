package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertStatus
import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.Match
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.RegistrationMatch
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.Status
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRegistrationService
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository

import spock.lang.Specification
import spock.lang.Subject

class AlertServiceSpec extends Specification {

  def alertMapper = Mock(AlertMapper)
  def alertRepository = Mock(AlertRepository)
  def alertRegistrationService = Mock(AlertRegistrationService)
  def registrationAlertResponseMapper = Mock(RegistrationAlertResponseMapper)

  @Subject
  def underTest = new AlertService(
      alertMapper,
      alertRepository,
      alertRegistrationService,
      registrationAlertResponseMapper
  )

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
        [
            RegisteredAlerts.AlertWithMatches.builder().build(),
            RegisteredAlerts.AlertWithMatches.builder().build()
        ]
    )

    def alerts = [
        RegistrationAlert.builder().id('alert_id_1').build(),
        RegistrationAlert.builder().id('alert_id_2').build()
    ]

    when:
    def response = underTest.registerAlertsAndMatches(command)

    then:
    1 * alertRepository.findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >> []
    1 * alertMapper.toAlertsToRegister(_ as List<AlertWithMatches>) >> alertsToRegister
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts
    1 * alertMapper.toAlerts(_ as RegisteredAlerts, alertsWithMatches, 'batch_id_1') >> alerts
    1 * alertRepository.saveAlerts(_ as List<Alert>)
    1 * alertMapper.toErrorAlerts([] as List<AlertWithMatches>, 'batch_id_1') >> []
    1 * registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(_ as List<Alert>) >> alerts
    1 * registrationAlertResponseMapper.fromAlertsWithMatchesToRegistrationAlerts(
        _ as List<com.silenteight.bridge.core.registration.domain.model.AlertWithMatches>) >> []
    response.size() == 2
  }

  def 'should register and save only alerts not existing in DB'() {
    given:
    def alertIdExistingInDb = 'alert_id_1'

    def alert1 = AlertWithMatches.builder()
        .alertId(alertIdExistingInDb)
        .alertMetadata('alertMetadata')
        .matches([new Match('match_id_11')])
        .alertStatus(AlertStatus.SUCCESS)
        .build()

    def alert2 = AlertWithMatches.builder()
        .alertId('alert_id_2')
        .alertMetadata('alertMetadata')
        .matches([new Match('match_id_21')])
        .alertStatus(AlertStatus.SUCCESS)
        .build()

    def command = new RegisterAlertsCommand('batch_id_1', [alert1, alert2])

    def alreadyRegisteredAlerts = [
        com.silenteight.bridge.core.registration.domain.model.AlertWithMatches.builder()
            .id(alertIdExistingInDb)
            .name('alertName')
            .metadata('alertMetadata')
            .matches(
                [
                    new com.silenteight.bridge.core.registration.domain.model.AlertWithMatches.Match(
                        'match_id_11', 'match_name')
                ]
            )
            .status(com.silenteight.bridge.core.registration.domain.model.AlertStatus.REGISTERED)
            .build()
    ]

    def alertsToRegister = new AlertsToRegister(
        [new AlertsToRegister.AlertWithMatches(
            'alert_id_1',
            [new AlertsToRegister.Match('match_id_11')]),
         new AlertsToRegister.AlertWithMatches(
             'alert_id_2',
             [new AlertsToRegister.Match('match_id_21')]),
        ])

    def registeredAlerts = new RegisteredAlerts(
        [
            RegisteredAlerts.AlertWithMatches.builder().build(),
            RegisteredAlerts.AlertWithMatches.builder().build()
        ]
    )

    def newAlerts = [RegistrationAlert.builder().id('alert_id_2').build()]

    def alreadyExistingAlerts = [
        RegistrationAlert.builder()
            .id(alertIdExistingInDb)
            .name('alertName')
            .status(Status.SUCCESS)
            .matches(
                [
                    RegistrationMatch.builder()
                        .id('match_id_11')
                        .name('match_name')
                        .build()
                ]
            )
            .build()
    ]

    when:
    def response = underTest.registerAlertsAndMatches(command)

    then:
    1 * alertRepository.findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >>
        alreadyRegisteredAlerts
    1 * alertMapper.toAlertsToRegister([alert2]) >> alertsToRegister
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts
    1 * alertMapper.toAlerts(_ as RegisteredAlerts, [alert2], 'batch_id_1') >> newAlerts
    1 * alertRepository.saveAlerts(_ as List<Alert>)
    1 * alertMapper.toErrorAlerts(_ as List<AlertWithMatches>, 'batch_id_1') >> []
    1 * registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(_ as List<Alert>) >> newAlerts
    1 * registrationAlertResponseMapper.fromAlertsWithMatchesToRegistrationAlerts(alreadyRegisteredAlerts) >>
        alreadyExistingAlerts
    response.size() == 2
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
        [
            RegisteredAlerts.AlertWithMatches.builder().build(),
            RegisteredAlerts.AlertWithMatches.builder().build()
        ]
    )

    def failedAlerts = [RegistrationAlert.builder().id('alert_id_1').status(Status.FAILURE).build()]
    def succeededAlerts = [RegistrationAlert.builder().id('alert_id_2').status(Status.SUCCESS).build()]

    when:
    def response = underTest.registerAlertsAndMatches(command)

    then:
    1 * alertRepository.findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >> []
    1 * alertMapper.toAlertsToRegister(_ as List<AlertWithMatches>) >> alertsToRegister
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts
    1 * alertMapper.toAlerts(_ as RegisteredAlerts, [succeededAlert], 'batch_id_1') >> [Alert.builder().build()]
    1 * alertRepository.saveAlerts(_ as List<Alert>)
    1 * alertMapper.toErrorAlerts(_ as List<AlertWithMatches>, 'batch_id_1') >> [Alert.builder().build()]
    1 * alertRepository.saveAlerts(_ as List<Alert>)
    1 * registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(_ as List<Alert>) >> failedAlerts
    1 * registrationAlertResponseMapper.fromAlertsToRegistrationAlerts(_ as List<Alert>) >> succeededAlerts
    1 * registrationAlertResponseMapper.fromAlertsWithMatchesToRegistrationAlerts(
        _ as List<com.silenteight.bridge.core.registration.domain.model.AlertWithMatches>) >> []
    response.size() == 2
  }

  def 'should update alert status to RECOMMENDED'() {
    given:
    def batchId = 'batchId'
    def alertNames = ['firstAlertName', 'secondAlertName']

    when:
    underTest.updateStatusToRecommended(batchId, alertNames)

    then:
    1 * alertRepository.updateStatusToRecommended(batchId, alertNames)
  }

  def 'should check that all alerts in batch have status RECOMMENDED'() {
    given:
    def batchId = 'batchId'

    when:
    def result = underTest.hasNoPendingAlerts(batchId)

    then:
    1 * alertRepository.countAllPendingAlerts(batchId) >> 0
    result == true
  }


  def 'should check that not all alerts in batch have status RECOMMENDED'() {
    given:
    def batchId = 'batchId'

    when:
    def result = underTest.hasNoPendingAlerts(batchId)

    then:
    1 * alertRepository.countAllPendingAlerts(batchId) >> 1
    result == false
  }
}
