package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationIncomingMatchFeatureInputSetFedProperties
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    properties = ["registration.analysis.mock-recommendations-generation=false"]
)
@ActiveProfiles("test")
@DirtiesContext
@Import(MatchFeatureInputFedReceivedFlowTestConfig.class)
class MatchFeatureInputFedReceivedFlowIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Autowired
  private AmqpRegistrationIncomingMatchFeatureInputSetFedProperties matchFeatureInputSetFedProperties

  @Autowired
  private BatchRepository batchRepository

  @Autowired
  private AlertRepository alertRepository

  @Unroll
  def "should receive a list of MatchFeatureInputSetFed messages and process UDS fed alerts"() {
    given:
    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)
    def batch = UdsFedFixtures.createBatch(isSimulationBatch, 2)
    def givenAlerts =
        [
            UdsFedFixtures.createAlert(batch.id(), "A_${UUID.randomUUID().toString()}", alertsStatus[0]),
            UdsFedFixtures.createAlert(batch.id(), "B_${UUID.randomUUID().toString()}", alertsStatus[1])
        ]
    def udsFedMessages =
        [
            UdsFedFixtures.createMessage(batch.id(), givenAlerts[0].name(), feedingStatuses[0]),
            UdsFedFixtures.createMessage(batch.id(), givenAlerts[1].name(), feedingStatuses[1]),
        ]

    and: 'given batch is saved in DB'
    batchRepository.create(batch)

    and: 'alerts for given batch are saved in DB'
    alertRepository.saveAlerts(givenAlerts)

    when: 'all messages MessageAlertMatchesFeatureInputFed are sent'
    udsFedMessages
        .each {msg -> rabbitTemplate.convertAndSend(matchFeatureInputSetFedProperties.exchangeName(), '', msg)}

    then:
    conditions.eventually {
      with(batchRepository.findById(batch.id()).get()) {
        and: 'batch status changed to expected'
        assert status() == expectedBatchStatus

        and: 'alerts and matches statuses changed to expected'
        def alerts = alertRepository.findAllByBatchIdAndNameIn(
            batch.id(),
            givenAlerts.collect {it.name()}
        ).stream().toList().sort {it.name()}
        alerts.eachWithIndex {alert, idx ->
          assert alert.status() == expectedAlertsStatus[idx]
          assert alert.errorDescription() == expectedAlertsErrorDesctiption[idx]
        }

        and: 'message BatchCompleted should be published if expected'
        if (isBatchCompletedExpected) {
          def messageBatchCompleted = getBatchCompletedMessage(batch.isSimulation())
          assert messageBatchCompleted.getBatchId() == batch.id()
          assert messageBatchCompleted.getBatchMetadata() == batch.batchMetadata()
          assert messageBatchCompleted.getAnalysisName() == expectedAnalysisName
        }
      }
    }

    where:
    isSimulationBatch | alertsStatus                                      | feedingStatuses                                || isBatchCompletedExpected | expectedBatchStatus    | expectedAnalysisName         | expectedAlertsStatus                              | expectedAlertsErrorDesctiption
    false             | [AlertStatus.REGISTERED, AlertStatus.REGISTERED]  | [FeedingStatus.SUCCESS, FeedingStatus.SUCCESS] || false                    | BatchStatus.PROCESSING | UdsFedFixtures.ANALYSIS_NAME | [AlertStatus.PROCESSING, AlertStatus.PROCESSING]  | [null, null]
    false             | [AlertStatus.ERROR, AlertStatus.REGISTERED]       | [FeedingStatus.SUCCESS, FeedingStatus.SUCCESS] || false                    | BatchStatus.PROCESSING | UdsFedFixtures.ANALYSIS_NAME | [AlertStatus.PROCESSING, AlertStatus.PROCESSING]  | [null, null]
    false             | [AlertStatus.REGISTERED, AlertStatus.RECOMMENDED] | [FeedingStatus.SUCCESS, FeedingStatus.SUCCESS] || false                    | BatchStatus.PROCESSING | UdsFedFixtures.ANALYSIS_NAME | [AlertStatus.PROCESSING, AlertStatus.RECOMMENDED] | [null, null]
    false             | [AlertStatus.REGISTERED, AlertStatus.REGISTERED]  | [FeedingStatus.FAILURE, FeedingStatus.SUCCESS] || false                    | BatchStatus.PROCESSING | UdsFedFixtures.ANALYSIS_NAME | [AlertStatus.ERROR, AlertStatus.PROCESSING]       | [UdsFedFixtures.ALERT_ERROR_DESCRIPTION, null]
    false             | [AlertStatus.RECOMMENDED, AlertStatus.REGISTERED] | [FeedingStatus.FAILURE, FeedingStatus.SUCCESS] || false                    | BatchStatus.PROCESSING | UdsFedFixtures.ANALYSIS_NAME | [AlertStatus.RECOMMENDED, AlertStatus.PROCESSING] | [null, null]
    false             | [AlertStatus.REGISTERED, AlertStatus.REGISTERED]  | [FeedingStatus.FAILURE, FeedingStatus.FAILURE] || true                     | BatchStatus.COMPLETED  | UdsFedFixtures.ANALYSIS_NAME | [AlertStatus.ERROR, AlertStatus.ERROR]            | [UdsFedFixtures.ALERT_ERROR_DESCRIPTION, UdsFedFixtures.ALERT_ERROR_DESCRIPTION]
    true              | [AlertStatus.REGISTERED, AlertStatus.REGISTERED]  | [FeedingStatus.SUCCESS, FeedingStatus.SUCCESS] || true                     | BatchStatus.COMPLETED  | ''                           | [AlertStatus.UDS_FED, AlertStatus.UDS_FED]        | [null, null]
    true              | [AlertStatus.REGISTERED, AlertStatus.REGISTERED]  | [FeedingStatus.FAILURE, FeedingStatus.SUCCESS] || true                     | BatchStatus.COMPLETED  | ''                           | [AlertStatus.ERROR, AlertStatus.UDS_FED]          | [UdsFedFixtures.ALERT_ERROR_DESCRIPTION, null]
    true              | [AlertStatus.REGISTERED, AlertStatus.REGISTERED]  | [FeedingStatus.FAILURE, FeedingStatus.FAILURE] || true                     | BatchStatus.COMPLETED  | ''                           | [AlertStatus.ERROR, AlertStatus.ERROR]            | [UdsFedFixtures.ALERT_ERROR_DESCRIPTION, UdsFedFixtures.ALERT_ERROR_DESCRIPTION]
  }

  private getBatchCompletedMessage(boolean isSimulationBatch) {
    if (isSimulationBatch) {
      return (MessageBatchCompleted) rabbitTemplate.receiveAndConvert(
          MatchFeatureInputFedReceivedFlowTestConfig.SIMULATION_TEST_QUEUE_NAME, 10000L)
    }
    (MessageBatchCompleted) rabbitTemplate.receiveAndConvert(
        MatchFeatureInputFedReceivedFlowTestConfig.SOLVING_TEST_QUEUE_NAME, 10000L)
  }
}
