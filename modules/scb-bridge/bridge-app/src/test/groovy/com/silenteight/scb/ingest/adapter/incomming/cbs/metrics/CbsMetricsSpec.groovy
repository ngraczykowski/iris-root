package com.silenteight.scb.ingest.adapter.incomming.cbs.metrics

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.AckCalledEvent
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.CbsCallFailedEvent
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event.RecomCalledEvent
import com.silenteight.scb.ingest.adapter.incomming.cbs.metrics.CbsMetrics.MetricNames

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import spock.lang.Specification

class CbsMetricsSpec extends Specification {

  MeterRegistry meterRegistry = new SimpleMeterRegistry()
  CbsMetrics underTest = new CbsMetricsConfiguration().cbsMetrics()

  def "recording metrics does not fail without meter registry"() {
    when:
    recomCall("999")
    ackCall("xxx", true)

    then:
    noExceptionThrown()
  }

  def "dummy event is not handled"() {
    given:
    underTest.bindTo(meterRegistry)

    when:
    underTest.cbsEventListener(new Object())

    then:
    noExceptionThrown()
  }

  def "counts function calls"() {
    given:
    underTest.bindTo(meterRegistry)

    when:
    ackCall("000", true)
    ackCall("001", true)
    ackCall("002", false)
    ackCall("003", false)
    ackCall("004", true)
    recomCall("0")
    recomCall("01")
    recomCall("002")

    then:
    ackCounter == 5
    recomCounter == 3
  }

  def "counts function failures"() {
    given:
    underTest.bindTo(meterRegistry)

    when:
    callFailed("ACK")
    callFailed("RECOM")
    callFailed("foo")
    callFailed("bar")

    then:
    errorsCounter == 4
    functionErrors("RECOM") == 1
    functionErrors("ACK") == 1
  }

  private void ackCall(String statusCode, boolean watchlistLevel) {
    def event = AckCalledEvent
        .builder()
        .statusCode(statusCode)
        .watchlistLevel(watchlistLevel)
        .build()

    underTest.cbsEventListener(event)
  }

  private void recomCall(String statusCode) {
    underTest.cbsEventListener(RecomCalledEvent.builder().statusCode(statusCode).build())
  }

  private void callFailed(String functionType) {
    underTest.cbsEventListener(CbsCallFailedEvent.builder().functionType(functionType).build())
  }

  private double getAckCounter() {
    sumCounter(MetricNames.ACK_CALL)
  }

  private double getRecomCounter() {
    sumCounter(MetricNames.RECOM_CALL)
  }

  private double getErrorsCounter() {
    sumCounter(MetricNames.CBS_CALL_ERROR)
  }

  private double functionErrors(String functionType) {
    sumTaggedCounter(MetricNames.CBS_CALL_ERROR, "function", functionType)
  }

  private double sumCounter(String counterName) {
    double counter = 0.0
    meterRegistry.get(counterName).counters().each {
      counter += it.count()
    }
    counter
  }

  private double sumTaggedCounter(String counterName, String... tags) {
    double counter = 0.0
    meterRegistry.get(counterName).tags(tags).counters().each {
      counter += it.count()
    }
    counter
  }
}
