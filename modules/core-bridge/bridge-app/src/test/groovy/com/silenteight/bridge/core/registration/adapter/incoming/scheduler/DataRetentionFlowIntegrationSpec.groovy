package com.silenteight.bridge.core.registration.adapter.incoming.scheduler

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.dataretention.api.v1.AlertsExpired

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import spock.util.concurrent.PollingConditions

import static com.silenteight.bridge.core.registration.adapter.incoming.scheduler.DataRetentionFlowFixtures.*

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    properties = ["registration.analysis.mock-recommendations-generation=false"]
)
@ActiveProfiles("test")
@DirtiesContext
@Import(DataRetentionFlowRabbitMqTestConfig.class)
class DataRetentionFlowIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  BatchRepository batchRepository

  @Autowired
  AlertRepository alertRepository

  @Autowired
  RegistrationFacade registrationFacade

  @Autowired
  RabbitTemplate rabbitTemplate

  def setup() {
    batchRepository.create(BATCH)
    alertRepository.saveAlerts(ALERTS)
  }

  @Transactional
  def 'should publish AlertsExpired and mark alerts as archived'() {
    given:
    def scheduler = new DataRetentionScheduler(registrationFacade, ALERTS_EXPIRED_PROPERTIES)
    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)

    when:
    scheduler.run()

    then:
    conditions.eventually {
      def message = (AlertsExpired) rabbitTemplate.receiveAndConvert(
          DataRetentionFlowRabbitMqTestConfig.ALERTS_EXPIRED_TEST_QUEUE_NAME, 10000L)
      with(message) {
        alertsDataList == EXPECTED_ALERTS_DATA
      }
      verifyAlertsAreArchivedAndHaveClearedMetadata()
    }
  }

  private def verifyAlertsAreArchivedAndHaveClearedMetadata() {
    alertRepository.findAllByBatchIdAndNameIn(BATCH.id(), [ALERT_1.name(), ALERT_2.name()]).each {
      assert it.metadata() == null
      assert it.isArchived()
    }
  }
}
