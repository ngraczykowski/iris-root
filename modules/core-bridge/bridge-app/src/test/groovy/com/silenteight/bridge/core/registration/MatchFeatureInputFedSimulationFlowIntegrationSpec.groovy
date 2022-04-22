package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationIncomingMatchFeatureInputSetFedProperties
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.util.concurrent.PollingConditions

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    properties = ["registration.analysis.mock-recommendations-generation=false"])
@Import(MatchFeatureInputFedSimulationFlowTestConfig.class)
@ActiveProfiles("test")
@DirtiesContext
class MatchFeatureInputFedSimulationFlowIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private BatchRepository batchRepository

  @Autowired
  private AlertRepository alertRepository

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Autowired
  AmqpRegistrationIncomingMatchFeatureInputSetFedProperties properties

  def 'should process UDS fed alerts and publish BatchCompleted event'() {
    given:
    def batch = MatchFeatureInputFedSimulationFlowFixtures.BATCH
    def alerts = MatchFeatureInputFedSimulationFlowFixtures.ALERTS
    def udsFedMessages = MatchFeatureInputFedSimulationFlowFixtures.ALERT_MATCHES_FEATURE_INPUT_FED_MESSAGES
    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)

    and: 'given batch is saved in DB'
    batchRepository.create(batch)

    and: 'alerts for given batch are saved in DB'
    alertRepository.saveAlerts(alerts)

    when: 'all messages MessageAlertMatchesFeatureInputFed are sent'
    udsFedMessages.each {msg -> rabbitTemplate.convertAndSend(properties.exchangeName(), '', msg)}

    then: 'message BatchCompleted should be published'
    noExceptionThrown()
    conditions.eventually {
      def messageBatchCompleted = (MessageBatchCompleted) rabbitTemplate.receiveAndConvert(
          MatchFeatureInputFedSimulationFlowTestConfig.TEST_QUEUE_NAME, 10000L)
      messageBatchCompleted.getBatchId() == MatchFeatureInputFedSimulationFlowFixtures.BATCH_ID
      messageBatchCompleted.getAnalysisName() == MatchFeatureInputFedSimulationFlowFixtures.ANALYSIS_NAME
      messageBatchCompleted.getBatchMetadata() == MatchFeatureInputFedSimulationFlowFixtures.BATCH_METADATA
    }
  }
}
