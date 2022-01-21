package com.silenteight.bridge.core.registration.domain.port.outgoing

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.Alert.Status
import com.silenteight.bridge.core.registration.domain.model.Match

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Subject

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class MatchRepositoryIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  @Subject
  private MatchRepository matchRepository

  @Autowired
  private AlertRepository alertRepository;

  def "should update statuses"() {
    given:
    def batchId = 'batchId'
    def alert1 = dummyAlert(batchId, 'alert1')
    def alert2 = dummyAlert(batchId, 'alert2')
    alertRepository.saveAlerts([alert1, alert2])

    when:
    matchRepository.updateStatusByBatchIdAndMatchIdInAndExternalAlertIdIn(
        Match.Status.ERROR,
        batchId,
        alert1.matches().collect {it.matchId()},
        [alert1.alertId()]
    )

    then:
    def alerts = alertRepository.findAllByBatchId(batchId)

    and: 'alert1 should have updated matches'
    with(alerts.find {it.alertId() == alert1.alertId()}) {alert ->
      alert.matches().each {match ->
        match.status() == Match.Status.ERROR
      }
    }

    and: 'alert1 should have NOT updated matches'
    with(alerts.find {it.alertId() == alert2.alertId()}) {alert ->
      alert.matches().each {match ->
        match.status() == Match.Status.REGISTERED
      }
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
                    .build(),
                Match.builder()
                    .matchId("{$alertId}_match_2_id")
                    .name("{$alertId}_match_2_name")
                    .status(Match.Status.REGISTERED)
                    .build()
            ])
        .build()
  }
}
