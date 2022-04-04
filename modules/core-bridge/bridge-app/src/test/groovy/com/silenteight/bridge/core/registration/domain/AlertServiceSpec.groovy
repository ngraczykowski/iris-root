package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.AlertStatus
import com.silenteight.bridge.core.registration.domain.command.RegisterAlertsCommand.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertsToRegister
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.RegisteredAlerts
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.Status
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRegistrationService
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.bridge.core.registration.domain.AlertServiceSpecHelper.buildAlert
import static com.silenteight.bridge.core.registration.domain.AlertServiceSpecHelper.buildAlertToRegister
import static com.silenteight.bridge.core.registration.domain.AlertServiceSpecHelper.buildRegisteredAlert
import static com.silenteight.bridge.core.registration.domain.AlertServiceSpecHelper.buildRegistrationAlert

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

  def 'two unregistered successful alerts: should save both alerts and matches'() {
    given:
    def priority = 1
    def alertsWithMatches = [
        buildAlert('alert_id_1', ['match_id_11'], AlertStatus.SUCCESS),
        buildAlert('alert_id_2', ['match_id_21'], AlertStatus.SUCCESS)
    ]

    def command = new RegisterAlertsCommand('batch_id_1', alertsWithMatches)

    def alertsToRegister = new AlertsToRegister(
        [
            buildAlertToRegister('alert_id_1', ['match_id_11']),
            buildAlertToRegister('alert_id_2', ['match_id_21'])
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
    def response = underTest.registerAlertsAndMatches(command, priority)

    then:
    with(alertRepository) {
      1 * findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >> []
      1 * saveAlerts(_ as List<Alert>)
    }
    with(alertMapper) {
      1 * toAlertsToRegister(_ as List<AlertWithMatches>, _ as Integer) >> alertsToRegister
      1 * toAlerts(_ as RegisteredAlerts, alertsWithMatches, 'batch_id_1') >> alerts
      1 * toErrorAlerts([] as List<AlertWithMatches>, 'batch_id_1') >> []
    }
    with(registrationAlertResponseMapper) {
      1 * fromAlertsToRegistrationAlerts(_ as List<Alert>) >> alerts
      1 * fromAlertsWithMatchesToRegistrationAlerts(
          _ as List<com.silenteight.bridge.core.registration.domain.model.AlertWithMatches>) >> []
    }
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts

    response.size() == 2
  }

  def 'one registered and one unregistered alerts: should save only unregistered alert and matches'() {
    given:
    def priority = 1
    def registeredId = "alert_id_1"
    def unregisteredId = "alert_id_2"
    def unregistered = buildAlert(unregisteredId, ['match_id_21'], AlertStatus.SUCCESS)

    def alertsWithMatches = [
        buildAlert(registeredId, ['match_id_11'], AlertStatus.SUCCESS),
        unregistered
    ]

    def command = new RegisterAlertsCommand('batch_id_1', alertsWithMatches)

    def alertsToRegister = new AlertsToRegister(
        [
            buildAlertToRegister(unregisteredId, ['match_id_21'])
        ])

    def registeredAlerts = new RegisteredAlerts(
        [
            RegisteredAlerts.AlertWithMatches.builder()
                .alertId(unregisteredId)
                .build()
        ]
    )

    def alerts = [RegistrationAlert.builder().id(unregisteredId).build()]

    def alreadyExistingAlerts = [buildRegistrationAlert(registeredId, Status.SUCCESS)]

    def alertsFromDB = [buildRegisteredAlert(registeredId)]

    when:
    def response = underTest.registerAlertsAndMatches(command, priority)

    then:
    with(alertRepository) {
      1 * findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >> alertsFromDB
      1 * saveAlerts(_ as List<Alert>)
    }
    with(alertMapper) {
      1 * toAlertsToRegister(_ as List<AlertWithMatches>, _ as Integer) >> alertsToRegister
      1 * toAlerts(_ as RegisteredAlerts, [unregistered], 'batch_id_1') >> alerts
      1 * toErrorAlerts([] as List<AlertWithMatches>, 'batch_id_1') >> []
    }
    with(registrationAlertResponseMapper) {
      1 * fromAlertsToRegistrationAlerts(_ as List<Alert>) >> alerts
      1 * fromAlertsWithMatchesToRegistrationAlerts(
          _ as List<com.silenteight.bridge.core.registration.domain.model.AlertWithMatches>) >>
          alreadyExistingAlerts
    }
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts

    response.size() == 2
  }

  def 'two registered successful alerts: should not register alerts'() {
    def priority = 1
    def firstRegisteredId = "alert_id_1"
    def secondRegisteredId = "alert_id_2"

    def alertsWithMatches = [
        buildAlert(firstRegisteredId, ['match_id_11'], AlertStatus.SUCCESS),
        buildAlert(secondRegisteredId, ['match_id_21'], AlertStatus.SUCCESS)
    ]

    def command = new RegisterAlertsCommand('batch_id_1', alertsWithMatches)

    def alreadyExistingAlerts = [
        buildRegistrationAlert(firstRegisteredId, Status.SUCCESS),
        buildRegistrationAlert(secondRegisteredId, Status.SUCCESS)
    ]

    def alertsFromDb = [
        buildRegisteredAlert(firstRegisteredId),
        buildRegisteredAlert(secondRegisteredId)
    ]

    when:
    def response = underTest.registerAlertsAndMatches(command, priority)

    then:
    with(alertRepository) {
      1 * findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >> alertsFromDb
      0 * saveAlerts(_ as List<Alert>)
    }
    with(alertMapper) {
      0 * toAlertsToRegister(_ as List<AlertWithMatches>, _ as Integer)
      0 * toAlerts(_ as RegisteredAlerts, [], 'batch_id_1')
      1 * toErrorAlerts([] as List<AlertWithMatches>, 'batch_id_1') >> []
    }
    with(registrationAlertResponseMapper) {
      0 * fromAlertsToRegistrationAlerts(_ as List<Alert>)
      1 * fromAlertsWithMatchesToRegistrationAlerts(
          _ as List<com.silenteight.bridge.core.registration.domain.model.AlertWithMatches>) >>
          alreadyExistingAlerts
    }
    0 * alertRegistrationService.registerAlerts(_ as AlertsToRegister)

    response.size() == 2
  }

  def 'two unregistered alerts with status SUCCESS and FAILURE: should save both alerts and matches'() {
    given:
    def priority = 1
    def successAlertId = 'alert_id_1'
    def failedAlertId = 'alert_id_2'

    def successAlert = buildAlert(successAlertId, ['match_id_11'], AlertStatus.SUCCESS)
    def failedAlert = buildAlert(failedAlertId, ['match_id_21'], AlertStatus.FAILURE)

    def alertsWithMatches = [
        successAlert,
        failedAlert
    ]

    def command = new RegisterAlertsCommand('batch_id_1', alertsWithMatches)

    def alertsToRegister = new AlertsToRegister(
        [
            buildAlertToRegister(successAlertId, ['match_id_11']),
            buildAlertToRegister(failedAlertId, ['match_id_21'])
        ])

    def registeredAlerts = new RegisteredAlerts(
        [
            RegisteredAlerts.AlertWithMatches.builder().build(),
        ]
    )

    def alerts = [buildRegistrationAlert(successAlertId, Status.SUCCESS)]

    def failedAlerts = [buildRegistrationAlert(failedAlertId, Status.FAILURE)]

    when:
    def response = underTest.registerAlertsAndMatches(command, priority)

    then:
    with(alertRepository) {
      1 * findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >> []
      2 * saveAlerts(_ as List<Alert>)
    }
    with(alertMapper) {
      1 * toAlertsToRegister(_ as List<AlertWithMatches>, _ as Integer) >> alertsToRegister
      1 * toAlerts(_ as RegisteredAlerts, [successAlert], 'batch_id_1') >> alerts
      1 * toErrorAlerts([failedAlert] as List<AlertWithMatches>, 'batch_id_1') >> failedAlerts
    }
    with(registrationAlertResponseMapper) {
      1 * fromAlertsToRegistrationAlerts(alerts as List<Alert>) >> alerts
      1 * fromAlertsToRegistrationAlerts(failedAlerts as List<Alert>) >> failedAlerts
      1 * fromAlertsWithMatchesToRegistrationAlerts([]) >> []
    }
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts

    response.size() == 2
  }

  def 'two alerts - registered FAILURE and unregistered SUCCESS: should register SUCCESS alert'() {
    given:
    def priority = 1
    def successAlertId = 'alert_id_1'
    def failedAlertId = 'alert_id_2'

    def successAlert = buildAlert(successAlertId, ['match_id_11'], AlertStatus.SUCCESS)
    def failedAlert = buildAlert(failedAlertId, ['match_id_21'], AlertStatus.FAILURE)

    def alertsWithMatches = [
        successAlert,
        failedAlert
    ]

    def command = new RegisterAlertsCommand('batch_id_1', alertsWithMatches)

    def alertsToRegister = new AlertsToRegister(
        [
            buildAlertToRegister(successAlertId, ['match_id_11']),
        ])

    def registeredAlerts = new RegisteredAlerts(
        [
            RegisteredAlerts.AlertWithMatches.builder().build(),
        ]
    )

    def registeredAlert = buildRegisteredAlert(failedAlertId)

    def alerts = [buildRegistrationAlert(successAlertId, Status.SUCCESS)]

    def failedAlerts = [buildRegistrationAlert(failedAlertId, Status.FAILURE)]

    when:
    def response = underTest.registerAlertsAndMatches(command, priority)

    then:
    with(alertRepository) {
      1 * findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >>
          [registeredAlert]
      1 * saveAlerts(_ as List<Alert>)
    }
    with(alertMapper) {
      1 * toAlertsToRegister(_ as List<AlertWithMatches>, _ as Integer) >> alertsToRegister
      1 * toAlerts(_ as RegisteredAlerts, [successAlert], 'batch_id_1') >> alerts
      0 * toErrorAlerts([failedAlert] as List<AlertWithMatches>, 'batch_id_1')
    }
    with(registrationAlertResponseMapper) {
      1 * fromAlertsToRegistrationAlerts(alerts as List<Alert>) >> alerts
      1 * fromAlertsWithMatchesToRegistrationAlerts([registeredAlert]) >> failedAlerts
    }
    1 * alertRegistrationService.registerAlerts(_ as AlertsToRegister) >> registeredAlerts

    response.size() == 2
  }

  def 'two alerts - unregistered FAILURE and registered SUCCESS: should register FAILURE alert'() {
    given:
    def priority = 1
    def successAlertId = 'alert_id_1'
    def failedAlertId = 'alert_id_2'

    def successAlert = buildAlert(successAlertId, ['match_id_11'], AlertStatus.SUCCESS)
    def failedAlert = buildAlert(failedAlertId, ['match_id_21'], AlertStatus.FAILURE)

    def alertsWithMatches = [
        successAlert,
        failedAlert
    ]

    def command = new RegisterAlertsCommand('batch_id_1', alertsWithMatches)

    def registeredAlert = buildRegisteredAlert(successAlertId)

    def alerts = [buildRegistrationAlert(successAlertId, Status.SUCCESS)]

    def failedAlerts = [buildRegistrationAlert(failedAlertId, Status.FAILURE)]

    when:
    def response = underTest.registerAlertsAndMatches(command, priority)

    then:
    with(alertRepository) {
      1 * findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >>
          [registeredAlert]
      1 * saveAlerts(_ as List<Alert>)
    }
    with(alertMapper) {
      0 * toAlertsToRegister(_ as List<AlertWithMatches>, _ as Integer)
      0 * toAlerts(_ as RegisteredAlerts, [successAlert], 'batch_id_1')
      1 * toErrorAlerts([failedAlert] as List<AlertWithMatches>, 'batch_id_1') >> failedAlerts
    }
    with(registrationAlertResponseMapper) {
      0 * fromAlertsToRegistrationAlerts(alerts as List<Alert>)
      1 * fromAlertsToRegistrationAlerts(failedAlerts as List<Alert>) >> failedAlerts
      1 * fromAlertsWithMatchesToRegistrationAlerts([registeredAlert]) >> alerts
    }
    0 * alertRegistrationService.registerAlerts(_ as AlertsToRegister)

    response.size() == 2
  }

  def 'two registered failure alerts: should not register alerts'() {
    def priority = 1
    def firstRegisteredId = "alert_id_1"
    def secondRegisteredId = "alert_id_2"

    def alertsWithMatches = [
        buildAlert(firstRegisteredId, ['match_id_11'], AlertStatus.FAILURE),
        buildAlert(secondRegisteredId, ['match_id_21'], AlertStatus.FAILURE)
    ]

    def command = new RegisterAlertsCommand('batch_id_1', alertsWithMatches)

    def alreadyExistingAlerts = [
        buildRegistrationAlert(firstRegisteredId, Status.FAILURE),
        buildRegistrationAlert(secondRegisteredId, Status.FAILURE)
    ]

    def alertsFromDb = [
        buildRegisteredAlert(firstRegisteredId),
        buildRegisteredAlert(secondRegisteredId)
    ]

    when:
    def response = underTest.registerAlertsAndMatches(command, priority)

    then:
    with(alertRepository) {
      1 * findAllWithMatchesByBatchIdAndAlertIdsIn(_ as String, _ as List<String>) >> alertsFromDb
      0 * saveAlerts(_ as List<Alert>)
    }
    with(alertMapper) {
      0 * toAlertsToRegister(_ as List<AlertWithMatches>, _ as Integer)
      0 * toAlerts(_ as RegisteredAlerts, [], 'batch_id_1')
      1 * toErrorAlerts([] as List<AlertWithMatches>, 'batch_id_1') >> []
    }
    with(registrationAlertResponseMapper) {
      0 * fromAlertsToRegistrationAlerts(_ as List<Alert>)
      1 * fromAlertsWithMatchesToRegistrationAlerts(
          _ as List<com.silenteight.bridge.core.registration.domain.model.AlertWithMatches>) >>
          alreadyExistingAlerts
    }
    0 * alertRegistrationService.registerAlerts(_ as AlertsToRegister)

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

  def 'should return true when all alerts in batch are in status RECOMMENDED, ERROR or DELIVERED'() {
    given:
    def batch = Batch.builder()
        .id('batchId')
        .alertsCount(0)
        .build()

    when:
    def result = underTest.hasNoPendingAlerts(batch)

    then:
    1 * alertRepository.countAllCompleted(batch.id()) >> 0
    result
  }


  def 'should return false when all alerts in batch are not in status DELIVERED, RECOMMENDED or ERROR'() {
    given:
    def batchId = Fixtures.BATCH_ID

    when:
    def result = underTest.hasNoPendingAlerts(RegistrationFixtures.BATCH)

    then:
    1 * alertRepository.countAllCompleted(batchId) >> 1
    !result
  }

  @Unroll
  def "should update alerts status to DELIVERED when alertNames #desc"() {
    given:
    def batchId = 'batchId'

    when:
    underTest.updateStatusToDelivered(batchId, alertNames)

    then:
    if (alertNames) {
      1 * alertRepository.updateStatusToDelivered(batchId, alertNames)
    } else {
      1 * alertRepository.updateStatusToDelivered(batchId)
    }

    where:
    alertNames || desc
    null       || 'is null'
    []         || 'is empty'
    ['alert1'] || 'is not empty'
  }
}
