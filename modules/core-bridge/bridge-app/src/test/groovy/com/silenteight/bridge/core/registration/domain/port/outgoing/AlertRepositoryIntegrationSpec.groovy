package com.silenteight.bridge.core.registration.domain.port.outgoing

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.Alert.Status
import com.silenteight.bridge.core.registration.domain.model.Match

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class AlertRepositoryIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private AlertRepository alertRepository;

  def 'should save alerts and then find alerts by given batchId'() {
    given:
    def batchIdIn = 'batch_id_' + UUID.randomUUID()
    def alertsToSave = [dummyAlert(batchIdIn, 'alert_id_' + UUID.randomUUID())]

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
        matchId() == alertsToSave.first().matches().first().matchId()
        name() == alertsToSave.first().matches().first().name()
        status() == alertsToSave.first().matches().first().status()
      }
    }
  }

  def 'should update status of given alert ids'() {
    given:
    def newStatus = Status.PROCESSING
    def batchId = 'batch_id_' + UUID.randomUUID()
    def alert1 = dummyAlert(batchId, 'alert_id_' + UUID.randomUUID())
    def alert2 = dummyAlert(batchId, 'alert_id_' + UUID.randomUUID())
    alertRepository.saveAlerts([alert1, alert2])

    when:
    alertRepository.updateStatusByBatchIdAndAlertIdIn(newStatus, batchId, [alert1.alertId()])

    then: 'alert1 should have updated status'
    with(alertRepository.findAllByBatchIdAndAlertIdIn(batchId, [alert1.alertId()]).first()) {
      status() == newStatus
    }

    and: 'alert2 should not have updated status'
    with(alertRepository.findAllByBatchIdAndAlertIdIn(batchId, [alert2.alertId()]).first()) {
      status() == alert2.status()
    }
  }

  private static def dummyAlert(String batchId, String alertId) {
    Alert.builder()
        .name("{$alertId}_name")
        .status(Status.REGISTERED)
        .alertId(alertId)
        .batchId(batchId)
        .matches(
            [
                Match.builder()
                    .matchId("{$alertId}_match_1_id")
                    .name("{$alertId}_match_1_name")
                    .status(Match.Status.REGISTERED)
                    .build()
            ])
        .build()
  }
}
