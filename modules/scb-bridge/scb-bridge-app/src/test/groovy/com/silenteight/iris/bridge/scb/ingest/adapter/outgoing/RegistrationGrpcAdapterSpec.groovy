/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.outgoing

import com.silenteight.registration.api.library.v1.*
import com.silenteight.iris.bridge.scb.ingest.fixtures.Fixtures

import spock.lang.Specification
import spock.lang.Subject

class RegistrationGrpcAdapterSpec extends Specification {

  def mapper = Mock(RegistrationMapper)
  def registrationServiceClient = Mock(RegistrationServiceClient)

  @Subject
  def underTest = new RegistrationGrpcAdapter(mapper, registrationServiceClient)

  def 'should register batch in core bridge'() {
    given:
    def batch = Fixtures.BATCH
    def batchIn = RegisterBatchIn.builder().build()

    when:
    underTest.registerBatch(batch)

    then:
    1 * mapper.toRegisterBatchIn(batch) >> batchIn
    1 * registrationServiceClient.registerBatch(batchIn)
    0 * _
  }

  def 'should register alerts and matches in core bridge'() {
    given:
    def request = Fixtures.REGISTRATION_REQUEST
    def response = Fixtures.REGISTRATION_RESPONSE
    def registerAlertsAndMatchesIn = RegisterAlertsAndMatchesIn.builder().build()
    def registerAlertsAndMatchesOut = RegisterAlertsAndMatchesOut.builder().build()

    when:
    underTest.registerAlertsAndMatches(request)

    then:
    1 * mapper.toRegisterAlertsAndMatchesIn(Fixtures.REGISTRATION_REQUEST) >> registerAlertsAndMatchesIn
    1 * registrationServiceClient.registerAlertsAndMatches(registerAlertsAndMatchesIn) >> registerAlertsAndMatchesOut
    1 * mapper.toRegistrationResponse(registerAlertsAndMatchesOut) >> response
    0 * _
  }
}
