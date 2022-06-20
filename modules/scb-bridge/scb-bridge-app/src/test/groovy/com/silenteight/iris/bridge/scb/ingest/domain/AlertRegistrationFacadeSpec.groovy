/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain

import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationResponse

import spock.lang.Specification
import spock.lang.Subject

class AlertRegistrationFacadeSpec extends Specification {

  def batchRegistrationService = Mock(BatchRegistrationService)
  def alertRegistrationService = Mock(AlertRegistrationService)

  @Subject
  def underTest = new AlertRegistrationFacade(batchRegistrationService, alertRegistrationService)

  def "should call batch and alert registration services"() {
    given:
    def internalBatchId = com
        .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator.generate()
    def alerts = [com
                      .silenteight
                      .iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.builder().build()]
    def context = RegistrationBatchContext.GNS_RT_CONTEXT

    when:
    underTest.registerAlerts(internalBatchId, alerts, context)

    then:
    1 * batchRegistrationService.register(internalBatchId, alerts, context)
    1 * alertRegistrationService.registerAlertsAndMatches(internalBatchId, alerts) >>
        RegistrationResponse.empty()
  }
}
