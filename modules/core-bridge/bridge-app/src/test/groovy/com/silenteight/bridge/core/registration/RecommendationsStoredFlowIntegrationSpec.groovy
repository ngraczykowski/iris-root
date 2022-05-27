package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationOutgoingRecommendationsStoredConfigurationProperties
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.proto.recommendation.api.v1.RecommendationsStored
import com.silenteight.proto.registration.api.v1.*
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub

import net.devh.boot.grpc.client.inject.GrpcClient
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
    properties = [
        "grpc.server.inProcessName=test",
        "grpc.client.inProcess.address=in-process:test",
        "registration.analysis.mock-recommendations-generation=false"
    ])
@Import(RecommendationsStoredFlowRabbitMqTestConfig.class)
@ActiveProfiles("test")
@DirtiesContext
class RecommendationsStoredFlowIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Autowired
  private BatchRepository batchRepository

  @Autowired
  private AlertRepository alertRepository

  @Autowired
  private RecommendationOutgoingRecommendationsStoredConfigurationProperties properties

  @GrpcClient("inProcess")
  private RegistrationServiceBlockingStub myService

  private static final BATCH_ID_INPUT = UUID.randomUUID().toString()
  private static final METADATA = '''
        {
          "someClientField": "123",
          "someSpecialClientData": "Lorem ipsum"
        }
        '''

  def 'should send RecommendationsStored message and after it check that all alerts have status RECOMMENDED, batch has status COMPLETED and message MessageBatchCompleted is published'() {
    given:
    def registerBatchRequest = createRegisterBatchRequest()

    and: 'register batch'

    myService.registerBatch(registerBatchRequest)

    def registerAlertsAndMatchesRequest = createRegisterAlertAndMatchesRequest()

    and: 'register alert and matches'

    myService.registerAlertsAndMatches(registerAlertsAndMatchesRequest)

    and: 'prepare recommendations stored message'

    def alertNames = alertRepository.findAllByBatchId(BATCH_ID_INPUT).stream()
        .map(alert -> alert.name())
        .toList()

    def analysisName = batchRepository.findById(BATCH_ID_INPUT).get().analysisName()
    def recommendationsStored = createRecommendationsStored(analysisName, alertNames)

    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)

    when:
    rabbitTemplate.convertAndSend(properties.exchangeName(), "", recommendationsStored)

    then:
    noExceptionThrown()
    conditions.eventually {
      def alerts = alertRepository.findAllByBatchId(BATCH_ID_INPUT)

      assert alerts.every {
        it.status().name() == 'RECOMMENDED'
      }

      def batch = batchRepository.findById(BATCH_ID_INPUT)

      assert batch.get().status() == BatchStatus.COMPLETED

      def messageBatchCompleted = (MessageBatchCompleted) rabbitTemplate.receiveAndConvert(
          RecommendationsStoredFlowRabbitMqTestConfig.TEST_BATCH_COMPLETED_QUEUE_NAME, 10000L)

      with(messageBatchCompleted) {
        batchId == BATCH_ID_INPUT
        analysisName == analysisName
        batchMetadata == METADATA
      }
    }
  }

  private static RegisterBatchRequest createRegisterBatchRequest() {
    RegisterBatchRequest.newBuilder()
        .setBatchId(BATCH_ID_INPUT)
        .setAlertCount(1)
        .setBatchMetadata(METADATA)
        .build()
  }

  private static RegisterAlertsAndMatchesRequest createRegisterAlertAndMatchesRequest() {
    def alertIdInput = UUID.randomUUID().toString()
    def matchIdInput = UUID.randomUUID().toString()

    def matchesInput = [
        Match.newBuilder()
            .setMatchId(matchIdInput)
            .build()
    ]

    def alertsWithMatches = [
        AlertWithMatches.newBuilder()
            .setAlertId(alertIdInput)
            .setStatus(AlertStatus.SUCCESS)
            .addAllMatches(matchesInput)
            .build()
    ]

    RegisterAlertsAndMatchesRequest.newBuilder()
        .setBatchId(BATCH_ID_INPUT)
        .addAllAlertsWithMatches(alertsWithMatches)
        .build()
  }

  private static RecommendationsStored createRecommendationsStored(
      String analysisName, List<String> alertNames) {
    RecommendationsStored.newBuilder()
        .setAnalysisName(analysisName)
        .addAllAlertNames(alertNames)
        .build()
  }
}
