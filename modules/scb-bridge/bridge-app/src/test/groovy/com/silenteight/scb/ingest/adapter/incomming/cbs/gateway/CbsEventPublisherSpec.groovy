package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class CbsEventPublisherSpec extends Specification {
  ApplicationEventPublisher eventPublisher = Mock(ApplicationEventPublisher)
  CbsEventPublisher underTest = new CbsEventPublisher()

  def "does not fail when publisher is not set"() {
    when:
    publishEvent { new Object() }

    then:
    noExceptionThrown()
  }

  def "passes event to event publisher"() {
    given:
    eventPublisherSet()
    def event = new Object()

    when:
    publishEvent { event }

    then:
    1 * eventPublisher.publishEvent(event)
  }

  def "does not fail when publication throws"() {
    given:
    eventPublisherSet()
    eventPublisher.publishEvent(_) >> { throw new RuntimeException() }

    when:
    publishEvent { new Object() }

    then:
    noExceptionThrown()
  }

  private eventPublisherSet() {
    underTest.setEventPublisher(eventPublisher)
  }

  private void publishEvent(Closure<Object> closure) {
    underTest.publishEvent(closure)
  }
}
