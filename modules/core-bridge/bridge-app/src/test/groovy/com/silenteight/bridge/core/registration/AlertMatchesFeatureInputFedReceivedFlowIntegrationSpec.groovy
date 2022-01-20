package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.Alert.Status
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.Match
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationIncomingMatchFeatureInputSetFedProperties
import com.silenteight.proto.registration.api.v1.*
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

  def "should receive a list of MatchFeatureInputSetFed messages, add alerts to analysis and update statuses"() {
    given: 'create 2 batches'
    def batches = (1..2).collect {
      def batchId = "batch_$it"
      def request = createRegisterBatchRequest(batchId)
      registrationService.registerBatch(request)
      batchId
    }

    and: 'per each batch, create 2 alerts with 3 matches'
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

          and: 'batch alerts statuses changed to PROCESSING'
          def alerts = alertRepository.findAllByBatchIdAndAlertIdIn(batchId, batchAlertIds[batchId])
          alerts.each {alert ->
            assert alert.status() == Status.PROCESSING
          }

          and: 'matches statuses changed to PROCESSING'
          def matches = alerts.collect {alert -> alert.matches()}.flatten() as List<Match>
          matches.each {match ->
            assert match.status() == Match.Status.PROCESSING
          }
        }
      }
    }
  }

  private static RegisterBatchRequest createRegisterBatchRequest(String batchId) {
    RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(123)
        .setBatchMetadata('')
        .build()
  }

  private static RegisterAlertsAndMatchesRequest createRegisterAlertAndMatchesRequest(
      String batchId) {

    def alertsWithMatches = (1..2).collect {
      def alertId = "alert_$it"
      AlertWithMatches.newBuilder()
          .setAlertId(alertId)
          .setStatus(AlertStatus.SUCCESS)
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
      com.silenteight.proto.registration.api.v1.Match.newBuilder()
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

  private static def createMessage(def batchId, def alertId, List<String> matchIds) {
    def fedMatches = matchIds.collect {matchId ->
      FedMatch.newBuilder()
          .setMatchId(matchId)
          .build()
    }
    MessageAlertMatchesFeatureInputFed.newBuilder()
        .setBatchId(batchId)
        .setAlertId(alertId)
        .addAllFedMatches(fedMatches)
        .build()
  }
}
