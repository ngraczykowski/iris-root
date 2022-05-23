package com.silenteight.scb.ingest.adapter.incomming.common.ingest

import com.silenteight.scb.feeding.domain.FeedingFacade
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext

import spock.lang.Specification
import spock.lang.Subject

class UdsFeedingPublisherImplSpec extends Specification {

  def feedingFacade = Mock(FeedingFacade)

  @Subject
  def underTest = new UdsFeedingPublisherImpl(1000, 8, 8, feedingFacade)

  def 'should successfully publish to uds'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alerts = Fixtures.alerts()

    when:
    underTest.publishToUds(internalBatchId, alerts, RegistrationBatchContext.CBS_CONTEXT)

    then:
    1 * feedingFacade.feedUds(
        {Alert it ->
          it.id() == alerts[0].id()
        })
    1 * feedingFacade.feedUds(
        {Alert it ->
          it.id() == alerts[1].id()
        })
    0 * _
  }

  def 'should failed publish to uds because of error'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alerts = Fixtures.alerts()
    feedingFacade.feedUds(_ as Alert) >> {it -> throw new IllegalStateException("fake error")}

    when:
    def result = underTest
        .publishToUds(internalBatchId, alerts, RegistrationBatchContext.CBS_CONTEXT)

    then:
    result.success().isEmpty()
    details(result.failed()) == details(alerts)
  }

  def 'should failed publish to uds because of timeout'() {
    given:
    underTest.timeoutMs = 1
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alerts = Fixtures.alerts()
    feedingFacade.feedUds(_ as Alert) >> {it ->
      Thread.sleep(100000)
    }

    when:
    def result = underTest
        .publishToUds(internalBatchId, alerts, RegistrationBatchContext.CBS_CONTEXT)

    then:
    result.success().isEmpty()
    details(result.failed()) == details(alerts)
  }

  static details(List<Alert> alerts) {
    alerts.stream()
        .map(x -> x.details())
        .toList()
  }

}
