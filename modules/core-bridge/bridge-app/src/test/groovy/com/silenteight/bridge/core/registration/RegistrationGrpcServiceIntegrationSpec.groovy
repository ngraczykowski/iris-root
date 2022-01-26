package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
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

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    properties = [
        "grpc.server.inProcessName=test",
        "grpc.server.port=-1",
        "grpc.client.inProcess.address=in-process:test"
    ]
)
@Import(NotifyBatchErrorFlowRabbitMqTestConfig.class)
@ActiveProfiles("test")
@DirtiesContext
class RegistrationGrpcServiceIntegrationSpec extends BaseSpecificationIT {

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Autowired
  private BatchRepository batchRepository

  @Autowired
  private AlertRepository alertRepository

  @GrpcClient("inProcess")
  private RegistrationServiceBlockingStub myService

  def "Should register batch"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def alertsSize = 123
    def metadata = """
        { 
          "someClientField": "123",
          "someSpecialClientData": "Lorem ipsum" 
        }
        """
    def registerBatchRequest = RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(alertsSize)
        .setBatchMetadata(metadata)
        .build()

    when:
    myService.registerBatch(registerBatchRequest)

    then:
    noExceptionThrown()

    def batch = batchRepository.findById(batchId)
    with(batch) {
      isPresent()
      with(get()) {
        !analysisName().empty
        BatchStatus.STORED == status()
        alertsCount() == alertsSize
        batchMetadata() == metadata
      }
    }
  }

  def "Should notify batch error"() {
    given:
    def id = UUID.randomUUID().toString()
    def error = "error occurred"
    def metadata = "batchMetadata"

    def notifyBatchErrorRequest = NotifyBatchErrorRequest.newBuilder()
        .setBatchId(id)
        .setErrorDescription(error)
        .setBatchMetadata(metadata)
        .build()

    when:
    myService.notifyBatchError(notifyBatchErrorRequest)

    then:
    noExceptionThrown()

    def batch = batchRepository.findById(id)

    with(batch) {
      isPresent()
      with(get()) {
        analysisName().empty
        BatchStatus.ERROR == status()
        errorDescription() == error
        alertsCount() == 0
        batchMetadata() == metadata
      }
    }

    and: "Batch Error event has been published"

    def messageBatchError = (MessageBatchError) rabbitTemplate
        .receiveAndConvert(NotifyBatchErrorFlowRabbitMqTestConfig.TEST_QUEUE_NAME, 10000L)

    with(messageBatchError) {
      batchId == id
      errorDescription == error
      batchMetadata == metadata
    }
  }

  def "Should register alerts with matches"() {
    given:
    def batchIdInput = UUID.randomUUID().toString()
    def alertIdInput = UUID.randomUUID().toString()
    def alertMetadata = """
        { 
          "someClientField": "123",
          "someSpecialClientData": "Lorem ipsum" 
        }
        """
    def matchIdInput = UUID.randomUUID().toString()

    def matchesInput = [
        Match.newBuilder()
            .setMatchId(matchIdInput)
            .build()
    ]

    def alertsWithMatches = [
        AlertWithMatches.newBuilder()
            .setAlertId(alertIdInput)
            .setStatus(com.silenteight.proto.registration.api.v1.AlertStatus.SUCCESS)
            .setAlertMetadata(alertMetadata)
            .addAllMatches(matchesInput)
            .build()
    ]

    def registerAlertsAndMatchesRequest = RegisterAlertsAndMatchesRequest.newBuilder()
        .setBatchId(batchIdInput)
        .addAllAlertsWithMatches(alertsWithMatches)
        .build()

    when:
    myService.registerAlertsAndMatches(registerAlertsAndMatchesRequest)

    then:
    noExceptionThrown()

    def alert = alertRepository.findAllByBatchId(batchIdInput)

    with(alert.first()) {
      !name().empty
      status() == AlertStatus.REGISTERED
      alertId() == alertIdInput
      batchId() == batchIdInput
      metadata() == alertMetadata
      matches().first().matchId() == matchIdInput
      !matches().first().name().empty
    }
  }
}
