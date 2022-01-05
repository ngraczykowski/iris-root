package com.silenteight.bridge.core.registration.domain.port.outgoing

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.Alert.Status
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class AlertRepositoryIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private AlertRepository alertRepository;

  def 'should save alerts and then find alerts by given batchId and alertIds'() {
    given:
    def batchIdIn = 'batch_id_1'
    def alertsToSave = [
        Alert.builder()
            .name('alert_1_name')
            .status(Status.REGISTERED)
            .alertId('alert_1_id')
            .batchId(batchIdIn)
            .matches(
                [
                    Match.builder()
                        .matchId('match_1_id')
                        .name('match_1_name')
                        .status(Match.Status.REGISTERED)
                        .build()
                ])
            .build()
    ]

    def alertIds = [alertsToSave.first().alertId()]

    when:
    alertRepository.saveAlerts(alertsToSave)

    then:
    def result = alertRepository.findByBatchIdAndAlertIdIn(batchIdIn, alertIds)

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
}
