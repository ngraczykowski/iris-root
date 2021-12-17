package com.silenteight.bridge.core

import com.silenteight.proto.registration.api.v1.RegisterBatchRequest
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub

import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = WebEnvironment.NONE, properties = [
    "grpc.server.inProcessName=test",
    "grpc.server.port=-1",
    "grpc.client.inProcess.address=in-process:test"
])
@DirtiesContext
class RegisterBatchIntegrationTest extends BaseSpecificationIT {

  @GrpcClient("inProcess")
  private RegistrationServiceBlockingStub myService

  def "Should register batch"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def registerBatchRequest = RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(123)
        .build()

    when:
    myService.registerBatch(registerBatchRequest)

    then:
    noExceptionThrown()
  }


}
