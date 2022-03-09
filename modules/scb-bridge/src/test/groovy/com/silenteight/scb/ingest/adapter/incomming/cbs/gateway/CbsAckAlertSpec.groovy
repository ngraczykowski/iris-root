package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway

import spock.lang.Specification

class CbsAckAlertSpec extends Specification {

  def 'should not allow to create CbsAckAlert without alertExternalId and batchId'() {
    when:
    new CbsAckAlert(alertExternalId, batchId)

    then:
    thrown(IllegalArgumentException)

    when:
    new CbsAckAlert(alertExternalId, batchId, false)

    then:
    thrown(IllegalArgumentException)

    where:
    alertExternalId | batchId
    null            | null
    ''              | ''
  }
}
