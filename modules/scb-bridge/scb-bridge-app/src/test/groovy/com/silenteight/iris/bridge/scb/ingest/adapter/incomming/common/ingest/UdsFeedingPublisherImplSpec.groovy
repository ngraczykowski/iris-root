/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest

import com.silenteight.iris.bridge.scb.feeding.domain.FeedingFacade
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext

import spock.lang.Specification
import spock.lang.Subject

class UdsFeedingPublisherImplSpec extends Specification {

  def feedingFacade = Mock(FeedingFacade)

  @Subject
  def underTest = new UdsFeedingPublisherImpl(1000, 8, 8, feedingFacade)

  def 'should successfully publish to uds'() {
    given:
    def internalBatchId = com
        .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator.generate()
    def alerts = Fixtures.alerts()

    when:
    underTest.publishToUds(internalBatchId, alerts, RegistrationBatchContext.CBS_CONTEXT)

    then:
    1 * feedingFacade.feedUds(
        {com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert it ->
          it.id() == alerts[0].id()
        }, RegistrationBatchContext.CBS_CONTEXT)
    1 * feedingFacade.feedUds(
        {com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert it ->
          it.id() == alerts[1].id()
        }, RegistrationBatchContext.CBS_CONTEXT)
    0 * _
  }

  def 'should failed publish to uds because of error'() {
    given:
    def internalBatchId = com
        .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator.generate()
    def alerts = Fixtures.alerts()
    feedingFacade.feedUds(_ as com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert, _ as RegistrationBatchContext) >>
        {it -> throw new IllegalStateException("fake error")}

    when:
    def result = underTest
        .publishToUds(internalBatchId, alerts, RegistrationBatchContext.CBS_CONTEXT)

    then:
    result.success().isEmpty()
    details(result.failed()) == details(alerts)
  }

  def 'should failed publish to uds because of timeout'() {
    given:
    underTest = new UdsFeedingPublisherImpl(1, 8, 8, feedingFacade)
    def internalBatchId = com
        .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator.generate()
    def alerts = Fixtures.alerts()
    feedingFacade.feedUds(_ as com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert, _ as RegistrationBatchContext) >> {it ->
      Thread.sleep(100000)
    }

    when:
    def result = underTest
        .publishToUds(internalBatchId, alerts, RegistrationBatchContext.CBS_CONTEXT)

    then:
    result.success().isEmpty()
    details(result.failed()) == details(alerts)
  }

  static details(List<com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert> alerts) {
    alerts.stream()
        .map(x -> x.details())
        .toList()
  }

}
