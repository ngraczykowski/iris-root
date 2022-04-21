package com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement

import spock.lang.Specification

import java.time.Duration

class TrafficManagerSpec extends Specification {

  private static Duration DURATION = Duration.ofSeconds(10)
  def timeSemaphore = Mock(TimeSemaphore)
  def semaphoreProperties = new GnsRtSemaphoreProperties(true, DURATION)
  def underTest = new TrafficManager(timeSemaphore, semaphoreProperties)

  def "Should return true when semaphore is active"() {
    given:
    timeSemaphore.isActive() >> true

    when:
    def result = underTest.holdPeriodicAlertProcessing()

    then:
    result
  }

  def "Should activate real time semaphore"() {
    when:
    underTest.activateRtSemaphore()

    then:
    1 * timeSemaphore.activate(DURATION.toSeconds())
  }

  def "Should not activate real time semaphore when it is disabled"() {
    given:
    semaphoreProperties = new GnsRtSemaphoreProperties(false, DURATION)
    underTest = new TrafficManager(timeSemaphore, semaphoreProperties)

    when:
    underTest.activateRtSemaphore()

    then:
    0 * timeSemaphore.activate(_)
  }
}
