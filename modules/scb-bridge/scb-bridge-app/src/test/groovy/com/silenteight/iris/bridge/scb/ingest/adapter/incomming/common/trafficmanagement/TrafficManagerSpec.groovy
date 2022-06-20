/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement

import spock.lang.Specification

import java.time.Duration

class TrafficManagerSpec extends Specification {

  private static Duration DURATION = Duration.ofSeconds(10)
  def timeSemaphore = Mock(TimeSemaphore)
  def loadManagementService = Mock(CbsAlertsLoadManagementService)
  def semaphoreProperties = new GnsRtSemaphoreProperties(true, DURATION)
  def underTest = new TrafficManager(timeSemaphore, semaphoreProperties, loadManagementService)

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
    underTest = new TrafficManager(timeSemaphore, semaphoreProperties, loadManagementService)

    when:
    underTest.activateRtSemaphore()

    then:
    0 * timeSemaphore.activate(_)
  }
}
