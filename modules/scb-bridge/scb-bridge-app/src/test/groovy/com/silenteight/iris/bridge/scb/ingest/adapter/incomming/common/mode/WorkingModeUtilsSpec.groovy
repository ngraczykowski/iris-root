/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode

import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import spock.lang.Specification

class WorkingModeUtilsSpec extends Specification {

  def "IsRealTimeSolvingModeEnabled - use cases"() {
    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode", List.class) >>
        workingMode

    when:
    def result = WorkingModeUtils.isRealTimeSolvingModeEnabled(context)

    then:
    result == expectedResult

    where:
    workingMode                               | expectedResult
    ["REAL_TIME_SOLVING", "PERIODIC_SOLVING"] | true
    ["REAL_TIME_SOLVING", "LEARNING"]         | true
    ["REAL_TIME_SOLVING"]                     | true
    ["real_time_solving"]                     | true
    ["ALL"]                                   | true
    ["all"]                                   | true
    ["NONE"]                                  | false
    ["ANOTHER_MODE"]                          | false
    []                                        | false
    null                                      | false
  }

  def "IsPeriodicSolvingModeEnabled - use cases"() {
    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode", List.class) >>
        workingMode

    when:
    def result = WorkingModeUtils.isPeriodicSolvingModeEnabled(context)

    then:
    result == expectedResult

    where:
    workingMode                               | expectedResult
    ["PERIODIC_SOLVING", "REAL_TIME_SOLVING"] | true
    ["PERIODIC_SOLVING", "LEARNING"]          | true
    ["PERIODIC_SOLVING"]                      | true
    ["periodic_solving"]                      | true
    ["ALL"]                                   | true
    ["all"]                                   | true
    ["NONE"]                                  | false
    ["ANOTHER_MODE"]                          | false
    []                                        | false
    null                                      | false
  }

  def "IsLearningModeEnabled - use cases"() {
    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode", List.class) >>
        workingMode

    when:
    def result = WorkingModeUtils.isLearningModeEnabled(context)

    then:
    result == expectedResult

    where:
    workingMode                       | expectedResult
    ["LEARNING", "REAL_TIME_SOLVING"] | true
    ["LEARNING", "PERIODIC_SOLVING"]  | true
    ["LEARNING"]                      | true
    ["learning"]                      | true
    ["ALL"]                           | true
    ["all"]                           | true
    ["NONE"]                          | false
    ["ANOTHER_MODE"]                  | false
    []                                | false
    null                              | false
  }
}
