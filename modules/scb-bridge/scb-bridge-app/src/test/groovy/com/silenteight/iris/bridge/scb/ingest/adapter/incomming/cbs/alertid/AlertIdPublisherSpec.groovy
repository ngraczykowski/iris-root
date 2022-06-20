/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService

import spock.lang.Specification

class AlertIdPublisherSpec extends Specification {

  def alertInFlightService = Mock(AlertInFlightService)
  def objectUnderTest = new AlertIdPublisher(alertInFlightService)

  def 'should consume alertIdCollection'() {
    given:
    def context = createContext()
    def someAlertId = createAlertId('systemId-1')
    def someOtherAlertId = createAlertId('systemId-2')
    def someAlertIds = [someAlertId, someOtherAlertId]

    when:
    objectUnderTest.accept(new AlertIdCollection(someAlertIds, context))

    then:
    1 * alertInFlightService.saveUniqueAlerts(someAlertIds, _ as ScbAlertIdContext)
  }

  def createAlertId(String systemId) {
    new AlertId(systemId, 'batchId')
  }

  def createContext() {
    AlertIdContext.builder()
        .ackRecords(true)
        .hitDetailsView("CBS_VIEW")
        .recordsView("VIEW")
        .priority(10)
        .watchlistLevel(true)
        .build()
  }
}
