/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode

import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class OnRealTimeAlertConditionSpec extends Specification {

  @Subject
  def underTest = new OnRealTimeAlertCondition()

  def "OnRealTimeAlertCondition - use cases"() {

    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode", List.class) >>
        workingMode

    when:
    def result = underTest.matches(context, null)

    then:
    result == expectedResult

    where:
    workingMode              | expectedResult
    ["REAL_TIME_SOLVING", "PERIODIC_SOLVING"] | true
    ["REAL_TIME_SOLVING"]                     | true
    ["real_time_solving"]                     | true
    ["ALL"]                                   | true
    ["all"]                                   | true
    ["NONE"]                                  | false
    ["ANOTHER_MODE"]                          | false
    []                                        | false
    null                                      | false
  }
}
