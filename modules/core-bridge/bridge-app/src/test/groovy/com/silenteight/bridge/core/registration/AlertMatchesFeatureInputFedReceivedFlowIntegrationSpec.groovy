package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationIncomingMatchFeatureInputSetFedProperties
import com.silenteight.proto.registration.api.v1.*
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub

import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.util.concurrent.PollingConditions

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    properties = [
        "grpc.server.inProcessName=test",
        "grpc.server.port=-1",
        "grpc.client.inProcess.address=in-process:test",
        "registration.analysis.mock-recommendations-generation=false"
    ])
@ActiveProfiles("test")
@DirtiesContext
class AlertMatchesFeatureInputFedReceivedFlowIntegrationSpec extends BaseSpecificationIT {

  static def BATCH_IDS = ['batch1', 'batch2']
  static def ALERT_IDS = ['alert1', 'alert2', 'alert3']
  static def ID_OF_ALERT_CONTAINING_FAILURE_FEEDING_STATUS = 'alert2'
  static def ID_OF_ALERT_CONTAINING_ALL_FAILED_MATCHES = 'alert3'

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Autowired
  private AmqpRegistrationIncomingMatchFeatureInputSetFedProperties matchFeatureInputSetFedProperties

  @Autowired
  private BatchRepository batchRepository

  @Autowired
  private AlertRepository alertRepository

  @GrpcClient("inProcess")
  private RegistrationServiceBlockingStub registrationService

  def "should receive a list of MatchFeatureInputSetFed messages, add alerts to analysis and update alert status"() {
    given: 'create 2 batches'
    def batches = BATCH_IDS.collect {batchId ->
      def request = createRegisterBatchRequest(batchId)
      registrationService.registerBatch(request)
      batchId
    }

    and: 'per each batch, create 3 alerts with 3 matches'
    Map<String, List<String>> batchAlertIds = [:]
    def alertsWithMatchesRequests = batches.collect {batchId ->
      def alertsWithMatchesRequest = createRegisterAlertAndMatchesRequest(batchId)
      registrationService.registerAlertsAndMatches(alertsWithMatchesRequest)
      def alertIds = alertsWithMatchesRequest.getAlertsWithMatchesList().collect {it.alertId}
      batchAlertIds[batchId] = alertIds
      alertsWithMatchesRequest
    }

    and: 'create messages'
    def messages = createMessages(alertsWithMatchesRequests)

    def conditions = new PollingConditions(timeout: 10, initialDelay: 0.5, factor: 1.25)

    when:
    messages.each {message ->
      rabbitTemplate.convertAndSend(matchFeatureInputSetFedProperties.exchangeName(), "", message)
    }

    then:
    conditions.eventually {
      batches.each {batchId ->
        with(batchRepository.findById(batchId).get()) {batch ->
          and: 'batch status changed to PROCESSING'
          assert batch.status() == BatchStatus.PROCESSING

          and: 'alerts and matches statuses changed to ERROR/PROCESSING'
          def alerts = alertRepository.findAllByBatchIdAndAlertIdIn(batchId, batchAlertIds[batchId])
          alerts.each {alert ->
            assert alert.status() == getExpectedAlertStatus(alert.alertId())
          }
        }
      }
    }
  }

  private static def createRegisterBatchRequest(String batchId) {
    RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(123)
        .setBatchMetadata('')
        .build()
  }

  private static def createRegisterAlertAndMatchesRequest(String batchId) {
    def alertsWithMatches = ALERT_IDS.collect {alertId ->
      AlertWithMatches.newBuilder()
          .setAlertId(alertId)
          .setStatus(com.silenteight.proto.registration.api.v1.AlertStatus.SUCCESS)
          .addAllMatches(createMatches(batchId, alertId))
          .build()
    }

    RegisterAlertsAndMatchesRequest.newBuilder()
        .setBatchId(batchId)
        .addAllAlertsWithMatches(alertsWithMatches)
        .build()
  }

  private static def createMatches(def batchId, def alertId) {
    (1..3).collect {
      Match.newBuilder()
          .setMatchId("match $it of alert $alertId of batch $batchId")
          .build()
    }
  }

  private static def createMessages(List<RegisterAlertsAndMatchesRequest> requests) {
    requests.collect {request ->
      request.getAlertsWithMatchesList().collect {alert ->
        createMessage(request.batchId, alert.alertId, alert.matchesList.collect {it.matchId})
      }
    }.flatten()
  }

  private static def createMessage(String batchId, String alertId, List<String> matchIds) {
    def fedMatches = matchIds.collect {matchId ->
      def feedingStatus = alertId == ID_OF_ALERT_CONTAINING_ALL_FAILED_MATCHES ?
                          FedMatch.FeedingStatus.FAILURE : FedMatch.FeedingStatus.SUCCESS
      FedMatch.newBuilder()
          .setMatchId(matchId)
          .setFeedingStatus(feedingStatus)
          .build()
    }
    def feedingStatus = alertId == ID_OF_ALERT_CONTAINING_FAILURE_FEEDING_STATUS ?
                        FeedingStatus.FAILURE :
                        FeedingStatus.SUCCESS
    MessageAlertMatchesFeatureInputFed.newBuilder()
        .setBatchId(batchId)
        .setAlertId(alertId)
        .setFeedingStatus(feedingStatus)
        .addAllFedMatches(fedMatches)
        .build()
  }

  private static def getExpectedAlertStatus(def alertId) {
    if (alertId == ID_OF_ALERT_CONTAINING_FAILURE_FEEDING_STATUS) {
      return AlertStatus.ERROR
    }
    AlertStatus.PROCESSING
  }
}
