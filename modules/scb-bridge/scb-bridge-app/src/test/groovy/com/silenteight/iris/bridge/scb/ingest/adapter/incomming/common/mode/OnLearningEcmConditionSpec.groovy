/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode

import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class OnLearningEcmConditionSpec extends Specification {

  @Subject
  def underTest = new OnLearningEcmCondition()

  def "OnLearningEcmCondition - use cases"() {

    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode", List.class) >>
        workingMode
    context.getEnvironment().getProperty("silenteight.scb-bridge.learning.ecm.enabled", _, _) >>
        learningEcmEnabled

    when:
    def result = underTest.matches(context, null)

    then:
    result == expectedResult

    where:
    workingMode                      | learningEcmEnabled | expectedResult
    ["LEARNING", "PERIODIC_SOLVING"] | true               | true
    ["LEARNING"]                     | true               | true
    ["learning"]                     | true               | true
    ["ALL"]                          | true               | true
    ["all"]                          | true               | true
    ["NONE"]                         | true               | false
    ["ANOTHER_MODE"]                 | true               | false
    []                               | true               | false
    null                             | true               | false
    ["LEARNING", "PERIODIC_SOLVING"] | false              | false
    ["LEARNING"]                     | false              | false
    ["learning"]                     | false              | false
    ["ALL"]                          | false              | false
    ["all"]                          | false              | false
    ["NONE"]                         | false              | false
    null                             | false              | false
  }
}
