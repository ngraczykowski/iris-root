package com.silenteight.bridge.core.registration.domain.port.outgoing

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.Match

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class AlertRepositoryIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private AlertRepository alertRepository

  def 'should save alerts and then find alerts by given batchId'() {
    given:
    def batchIdIn = 'batch_id_' + UUID.randomUUID()
    def alertsToSave = [dummyAlert(batchIdIn, 'alert_id_' + UUID.randomUUID(), 'match_id_' + UUID.randomUUID())]

    when:
    alertRepository.saveAlerts(alertsToSave)

    then:
    def result = alertRepository.findAllByBatchId(batchIdIn)

    with(result.first()) {
      name() == alertsToSave.first().name()
      alertId() == alertsToSave.first().alertId()
      status() == alertsToSave.first().status()
      batchId() == alertsToSave.first().batchId()
      with(matches().first()) {
        name() == alertsToSave.first().matches().first().name()
        matchId() == alertsToSave.first().matches().first().matchId()
      }
    }
  }

  def 'should update status of given alert ids to PROCESSING if alert is not in the given status already'() {
    given:
    def batchId = 'batch_id_' + UUID.randomUUID()
    def alert1 = dummyAlert(batchId, 'alert_id_' + UUID.randomUUID(), 'match_id_' + UUID.randomUUID(), AlertStatus.REGISTERED)
    def alert2 = dummyAlert(batchId, 'alert_id_' + UUID.randomUUID(), 'match_id_' + UUID.randomUUID(), AlertStatus.REGISTERED)
    def alert3 = dummyAlert(batchId, 'alert_id_' + UUID.randomUUID(), 'match_id_' + UUID.randomUUID(), AlertStatus.RECOMMENDED)
    alertRepository.saveAlerts([alert1, alert2, alert3])

    when:
    alertRepository.updateStatusToProcessing(batchId, [alert1.name(), alert3.name()], EnumSet.of(AlertStatus.RECOMMENDED))

    then: 'alert1 should have updated status'
    with(alertRepository.findAllByBatchIdAndNameIn(batchId, [alert1.name()]).first()) {
      status() == AlertStatus.PROCESSING
    }

    and: 'alert2 should not have updated status'
    with(alertRepository.findAllByBatchIdAndNameIn(batchId, [alert2.name()]).first()) {
      status() == alert2.status()
    }

    and: 'alert3 should not have updated status'
    with(alertRepository.findAllByBatchIdAndNameIn(batchId, [alert3.name()]).first()) {
      status() == alert3.status()
    }
  }

  def 'should return alerts and matches by batch id'() {
    given:
    def batchId = 'batch_id_' + UUID.randomUUID()
    def alertsToSave = [
        dummyAlert(batchId, 'alert_id_' + UUID.randomUUID(), 'match_id_' + UUID.randomUUID()),
        dummyAlert(batchId, 'alert_id_' + UUID.randomUUID(), 'match_id_' + UUID.randomUUID())
    ]
    alertRepository.saveAlerts(alertsToSave)

    when:
    def results = alertRepository.findAllWithMatchesByBatchId(batchId)

    then:
    results.size() == 2
  }

  private static def dummyAlert(String batchId, String alertId, String matchId, AlertStatus status) {
    Alert.builder()
        .name("alerts/{$alertId}")
        .status(status)
        .alertId(alertId)
        .batchId(batchId)
        .matches(
            [
                new Match("matches/{$matchId}", matchId)
            ]
        )
        .build()
  }

  private static def dummyAlert(String batchId, String alertId, String matchId) {
    return dummyAlert(batchId, alertId, matchId, AlertStatus.REGISTERED)
  }
}
