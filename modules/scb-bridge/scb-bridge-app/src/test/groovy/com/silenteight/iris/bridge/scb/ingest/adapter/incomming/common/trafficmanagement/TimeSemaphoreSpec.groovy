/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement

import spock.lang.Specification
import spock.lang.Subject

class TimeSemaphoreSpec extends Specification {

  @Subject
  def underTest = new TimeSemaphore()

  def "should be active when method activate is called"() {
    given:
    def seconds = 10L

    when:
    underTest.activate(seconds)

    then:
    underTest.isActive()
  }

  def "should not be active when time is set to 0"() {
    given:
    def seconds = 0L

    when:
    underTest.activate(seconds)

    then:
    !underTest.isActive()
  }
}
