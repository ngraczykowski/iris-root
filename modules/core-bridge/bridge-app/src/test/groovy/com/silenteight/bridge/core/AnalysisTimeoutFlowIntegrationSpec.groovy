package com.silenteight.bridge.core

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendedAction
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
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

import static com.silenteight.bridge.core.registration.domain.model.AlertStatus.ERROR
import static com.silenteight.bridge.core.registration.domain.model.AlertStatus.RECOMMENDED
import static com.silenteight.proto.registration.api.v1.AlertStatus.FAILURE
import static com.silenteight.proto.registration.api.v1.AlertStatus.SUCCESS

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    properties = [
        "grpc.server.inProcessName=test",
        "grpc.server.port=-1",
        "grpc.client.inProcess.address=in-process:test",
        "registration.analysis.mock-recommendations-generation=false",
        "amqp.registration.outgoing.verify-batch-timeout.delay-time:2s"
    ]
)
@ActiveProfiles("test")
@DirtiesContext
@Import(AnalysisTimeoutFlowRabbitMqTestConfig.class)
class AnalysisTimeoutFlowIntegrationSpec extends BaseSpecificationIT {

  @GrpcClient("inProcess")
  private RegistrationServiceBlockingStub registrationGrpcService

  @Autowired
  private BatchRepository batchRepository

  @Autowired
  private RecommendationRepository recommendationRepository

  @Autowired
  private AlertRepository alertRepository

  @Autowired
  RabbitTemplate rabbitTemplate

  static def alertsSize = 4
  static def batchMetadata = "{}"

  def 'should create custom recommendations and complete batch when it got timed out'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def registerBatchRequest = RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(alertsSize)
        .setBatchMetadata(batchMetadata)
        .build()

    def registerAlertAndMatchesRequest = createRegisterAlertAndMatchesRequest(batchId, SUCCESS)
    def conditions = new PollingConditions(timeout: 10, initialDelay: 2, factor: 1)

    when:
    registrationGrpcService.registerBatch(registerBatchRequest)
    registrationGrpcService.registerAlertsAndMatches(registerAlertAndMatchesRequest)

    then:
    conditions.eventually {

      and: 'batch status was changed to COMPLETED'
      def batch = batchRepository.findById(batchId).get()
      assert batch.status() == BatchStatus.COMPLETED

      and: 'custom recommendations were created'
      def recommendations = recommendationRepository.findByAnalysisName(batch.analysisName())
      assert recommendations.size() == alertsSize
      recommendations.each {verifyRecommendationWasTimedOut(it)}

      and: 'alerts status was changed to RECOMMENDED'
      def alerts = alertRepository.findAllByBatchId(batchId)
      alerts.each {alert ->
        assert alert.status() == RECOMMENDED
      }

      and: 'BatchCompleted event was published'
      def messageBatchCompleted = (MessageBatchCompleted) rabbitTemplate.receiveAndConvert(
          AnalysisTimeoutFlowRabbitMqTestConfig.TEST_BATCH_COMPLETED_QUEUE_NAME, 1000L)

      verifyMessageBatchCompleted(messageBatchCompleted, batch.analysisName())
    }
  }

  def 'should complete batch when batch is in STORED status and all alerts are in ERROR status'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def registerBatchRequest = RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(alertsSize)
        .setBatchMetadata(batchMetadata)
        .build()

    def registerAlertAndMatchesRequest = createRegisterAlertAndMatchesRequest(batchId, FAILURE)
    def conditions = new PollingConditions(timeout: 10, initialDelay: 2, factor: 1)

    when:
    registrationGrpcService.registerBatch(registerBatchRequest)
    registrationGrpcService.registerAlertsAndMatches(registerAlertAndMatchesRequest)

    then:
    conditions.eventually {

      and: 'batch status was changed to COMPLETED'
      def batch = batchRepository.findById(batchId).get()
      assert batch.status() == BatchStatus.COMPLETED

      and: 'no recommendations were created'
      def recommendations = recommendationRepository.findByAnalysisName(batch.analysisName())
      assert recommendations.size() == 0

      and: 'alerts status is ERROR'
      def alerts = alertRepository.findAllByBatchId(batchId)
      alerts.each {alert ->
        assert alert.status() == ERROR
      }

      and: 'BatchCompleted event was published'
      def messageBatchCompleted = (MessageBatchCompleted) rabbitTemplate.receiveAndConvert(
          AnalysisTimeoutFlowRabbitMqTestConfig.TEST_BATCH_COMPLETED_QUEUE_NAME, 1000L)
      verifyMessageBatchCompleted(messageBatchCompleted, batch.analysisName())
    }
  }

  private static def createRegisterAlertAndMatchesRequest(String batchId, AlertStatus status) {
    def alertsWithMatches = (1..alertsSize).collect {alertId ->
      def matches = [Match.newBuilder()
                         .setMatchId("match of alert $alertId")
                         .build()]
      AlertWithMatches.newBuilder()
          .setAlertId(alertId.toString())
          .setStatus(status)
          .addAllMatches(matches)
          .build()
    }

    RegisterAlertsAndMatchesRequest.newBuilder()
        .setBatchId(batchId)
        .addAllAlertsWithMatches(alertsWithMatches)
        .build()
  }

  private def verifyRecommendationWasTimedOut(RecommendationWithMetadata recommendation) {
    with(recommendation) {
      name() == ''
      recommendedAction() == RecommendedAction.ACTION_INVESTIGATE.name()
      recommendationComment() == ''
      timeout()
    }
  }

  private def verifyMessageBatchCompleted(MessageBatchCompleted message, String analysisName) {
    with(message) {
      it.batchId == batchId
      it.analysisName == analysisName
      it.batchMetadata == batchMetadata
    }
  }
}
