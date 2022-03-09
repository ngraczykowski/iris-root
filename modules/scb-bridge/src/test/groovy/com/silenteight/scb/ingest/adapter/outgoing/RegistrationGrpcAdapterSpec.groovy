package com.silenteight.scb.ingest.adapter.outgoing

import com.silenteight.registration.api.library.v1.*
import com.silenteight.scb.ingest.fixtures.Fixtures

import spock.lang.Specification
import spock.lang.Subject

class RegistrationGrpcAdapterSpec extends Specification {

  def registrationServiceClient = Mock(RegistrationServiceClient)

  @Subject
  def underTest = new RegistrationGrpcAdapter(registrationServiceClient)

  def 'should register batch in core bridge'() {
    given:
    def batch = Fixtures.BATCH
    def batchIn = RegisterBatchIn.builder().build()

    when:
    underTest.registerBatch(batch)

    then:
    1 * registrationServiceClient.registerBatch(batchIn)
    0 * _
  }

}
