package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationOutgoingRecommendationsReceivedConfigurationProperties
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.proto.recommendation.api.v1.RecommendationsReceived
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
        "grpc.server.port=-1",
        "grpc.client.inProcess.address=in-process:test"
    ])
@Import(RecommendationReceivedFlowRabbitMqTestConfig.class)
@ActiveProfiles("test")
@DirtiesContext
class RecommendationReceivedFlowIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Autowired
  private BatchRepository batchRepository

  @Autowired
  private AlertRepository alertRepository

  @Autowired
  private RecommendationOutgoingRecommendationsReceivedConfigurationProperties properties

  @GrpcClient("inProcess")
  private RegistrationServiceBlockingStub myService

  private static final BATCH_ID_INPUT = UUID.randomUUID().toString()
  private static final ANALYSIS_NAME = 'analysis_name'
  private static final METADATA = '''
        { 
          "someClientField": "123",
          "someSpecialClientData": "Lorem ipsum" 
        }
        '''

  def 'should send recommendationsReceived message and after it check that all alerts and matches have status RECOMMENDED, batch has status COMPLETED and message MessageBatchCompleted is published'() {
    given:
    def registerBatchRequest = createRegisterBatchRequest()

    and: 'register batch'

    myService.registerBatch(registerBatchRequest)

    def registerAlertsAndMatchesRequest = createRegisterAlertAndMatchesRequest()

    and: 'register alert and matches'

    myService.registerAlertsAndMatches(registerAlertsAndMatchesRequest)

    and: 'prepare recommendations received message'

    def alertNames = alertRepository.findAllByBatchId(BATCH_ID_INPUT).stream()
        .map(alert -> alert.name())
        .toList()

    def recommendationsReceived = createRecommendationsReceived(alertNames)

    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, factor: 1.25)

    when:
    rabbitTemplate.convertAndSend(properties.exchangeName(), "", recommendationsReceived)

    then:
    noExceptionThrown()
    conditions.eventually {
      def alerts = alertRepository.findAllByBatchId(BATCH_ID_INPUT)

      assert alerts.every {
        it.status().name() == 'RECOMMENDED'
        it.matches().every {it.status().name() == 'RECOMMENDED'}
      }

      def batch = batchRepository.findById(BATCH_ID_INPUT)

      assert batch.get().status() == BatchStatus.COMPLETED

      def messageBatchCompleted = (MessageBatchCompleted) rabbitTemplate.receiveAndConvert(
          RecommendationReceivedFlowRabbitMqTestConfig.TEST_BATCH_COMPLETED_QUEUE_NAME, 10000L)

      with(messageBatchCompleted) {
        batchId == BATCH_ID_INPUT
        analysisId == ANALYSIS_NAME
        alertIdsCount == 1
        batchMetadata == METADATA
      }
    }
  }

  private static RegisterBatchRequest createRegisterBatchRequest() {
    RegisterBatchRequest.newBuilder()
        .setBatchId(BATCH_ID_INPUT)
        .setAlertCount(123)
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

  private static RecommendationsReceived createRecommendationsReceived(List<String> alertNAmes) {
    RecommendationsReceived.newBuilder()
        .setAnalysisId(ANALYSIS_NAME)
        .addAllAlertIds(alertNAmes)
        .build()
  }
}
