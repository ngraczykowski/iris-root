package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.BaseSpecificationIT
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.infrastructure.amqp.AmqpRegistrationOutgoingNotifyBatchErrorProperties
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

  def 'should register batch for #scenario'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def alertsSize = 123
    def priority = 1
    def metadata = '''
        {
          "someClientField": "123",
          "someSpecialClientData": "Lorem ipsum"
        }
        '''
    def registerBatchRequest = RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setBatchPriority(priority)
        .setAlertCount(alertsSize)
        .setBatchMetadata(metadata)
        .setIsSimulation(isSimulation)
        .build()

    when:
    myService.registerBatch(registerBatchRequest)

    then:
    noExceptionThrown()

    def batch = batchRepository.findById(batchId)
    with(batch) {
      isPresent()
      with(get()) {
        it.analysisName()?.empty == analysisNameCheck
        it.status() == batchStatus
        it.alertsCount() == alertsSize
        it.batchMetadata() == metadata
        it.batchPriority() == priority
        it.isSimulation() == isSimulation
      }
    }

    where:
    isSimulation | batchStatus        | analysisNameCheck | scenario
    false        | BatchStatus.STORED | false             | 'solving'
    true         | BatchStatus.STORED | null              | 'simulation'
  }

  def 'should notify batch error for #scenario'() {
    given:
    def id = UUID.randomUUID().toString()
    def error = 'error occurred'
    def metadata = 'batchMetadata'

    def notifyBatchErrorRequest = NotifyBatchErrorRequest.newBuilder()
        .setBatchId(id)
        .setErrorDescription(error)
        .setBatchMetadata(metadata)
        .setIsSimulation(isSimulation)
        .build()

    when:
    myService.notifyBatchError(notifyBatchErrorRequest)

    then:
    noExceptionThrown()

    def batch = batchRepository.findById(id)

    with(batch) {
      isPresent()
      with(get()) {
        it.analysisName().empty
        it.status() == batchStatus
        it.errorDescription() == error
        it.alertsCount() == 0
        it.batchMetadata() == metadata
        it.isSimulation() == isSimulation
      }
    }

    and: 'Batch Error event has been published'

    def messageBatchError = (MessageBatchError) rabbitTemplate
        .receiveAndConvert(NotifyBatchErrorFlowRabbitMqTestConfig.TEST_QUEUE_NAME, 10000L)

    with(messageBatchError) {
      batchId == id
      errorDescription == error
      batchMetadata == metadata
    }

    where:
    isSimulation | batchStatus       | scenario
    false        | BatchStatus.ERROR | 'solving'
    true         | BatchStatus.ERROR | 'simulation'
  }

  def 'should register alerts with matches'() {
    given:
    def batchIdInput = UUID.randomUUID().toString()
    def alertIdInput = UUID.randomUUID().toString()
    def alertMetadata = '''
        {
          "someClientField": "123",
          "someSpecialClientData": "Lorem ipsum"
        }
        '''
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

    def analysisName = UUID.randomUUID().toString()
    def policyName = UUID.randomUUID().toString()
    def batch = Batch.builder()
        .id(batchIdInput)
        .analysisName(analysisName)
        .policyName(policyName)
        .alertsCount(123)
        .batchMetadata('batchMetadata')
        .status(BatchStatus.STORED)
        .batchPriority(1)
        .build()

    and: 'Batch with priority was created'
    batchRepository.create(batch)

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
