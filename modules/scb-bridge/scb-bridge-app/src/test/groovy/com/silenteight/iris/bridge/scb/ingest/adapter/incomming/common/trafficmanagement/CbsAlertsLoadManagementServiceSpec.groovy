/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService

import spock.lang.Specification
import spock.lang.Unroll

class CbsAlertsLoadManagementServiceSpec extends Specification {

  @Unroll
  def 'should return #expected when maxLoadThreshold=#maxLoadThreshold and alertsCountWithInProgressState=#alertsCountWithInProgressState'() {
    given:
    def fairLoadThreshold = 2000L
    def alertInFlightService = Mock(AlertInFlightService)
    alertInFlightService.getAckAlertsCount() >> alertsCountWithInProgressState
    def loadManagementProperties = new CbsAlertsLoadManagementProperties(
        true, maxLoadThreshold, fairLoadThreshold)
    def underTest = new CbsAlertsLoadManagementService(
        alertInFlightService, loadManagementProperties)

    when:
    def result = underTest.isReadyToLoad()

    then:
    result == expected

    where:
    maxLoadThreshold | alertsCountWithInProgressState | expected
    5000L            | 10L                            | true
    10000L           | 12000L                         | false
  }
}
