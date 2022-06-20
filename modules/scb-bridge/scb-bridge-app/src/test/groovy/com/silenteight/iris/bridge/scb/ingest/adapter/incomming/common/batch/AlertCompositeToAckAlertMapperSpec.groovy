/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch

import spock.lang.Specification
import spock.lang.Unroll

class AlertCompositeToAckAlertMapperSpec extends Specification {

  def 'should do not allow to create ack alert from invalid scbAlertDetails'() {
    given:
    def scbAlertDetails = com
        .silenteight
        .iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails.builder().build()

    when:
    AlertCompositeToAckAlertMapper.toAckAlert(scbAlertDetails)

    then:
    thrown(IllegalArgumentException)
  }

  @Unroll
  def "should map alert composite to ack alert when watchlistId = #watchlistId"() {
    given:
    def scbAlertDetails = com
        .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails
        .builder()
        .batchId('batch-id')
        .systemId('system-id')
        .watchlistId(watchlistId)
        .build()

    when:
    def result = AlertCompositeToAckAlertMapper.toAckAlert(scbAlertDetails)

    then:
    result.alertExternalId == 'system-id'
    result.batchId == 'batch-id'
    result.watchlistLevel == expectedFlag

    where:
    watchlistId | expectedFlag
    ''          | false
    '1'         | true
  }
}
