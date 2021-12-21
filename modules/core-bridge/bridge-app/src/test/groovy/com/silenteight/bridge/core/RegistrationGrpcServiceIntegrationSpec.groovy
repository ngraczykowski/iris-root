package com.silenteight.bridge.core

import com.silenteight.bridge.core.registration.domain.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.proto.registration.api.v1.NotifyBatchErrorRequest
import com.silenteight.proto.registration.api.v1.RegisterBatchRequest
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub

import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = [
    "grpc.server.inProcessName=test",
    "grpc.server.port=-1",
    "grpc.client.inProcess.address=in-process:test"
])
@ActiveProfiles("test")
@DirtiesContext
class RegistrationGrpcServiceIntegrationSpec extends BaseSpecificationIT {

  @GrpcClient("inProcess")
  private RegistrationServiceBlockingStub myService

  @Autowired
  private BatchRepository batchRepository

  def "Should register batch"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def alertsSize = 123
    def registerBatchRequest = RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(alertsSize)
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
      }
    }
  }

  def "Should notify batch error"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def notifyBatchErrorRequest = NotifyBatchErrorRequest.newBuilder()
        .setBatchId(batchId)
        .build()

    when:
    myService.notifyBatchError(notifyBatchErrorRequest)

    then:
    noExceptionThrown()

    def batch = batchRepository.findById(batchId)
    with(batch) {
      isPresent()
      with(get()) {
        analysisName().empty
        BatchStatus.ERROR == status()
        alertsCount() == 0
      }
    }
  }
}
