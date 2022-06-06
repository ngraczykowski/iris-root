package com.silenteight.scb.ingest.adapter.incomming.common.mode

import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class OnLearningAlertConditionSpec extends Specification {

  @Subject
  def underTest = new OnLearningAlertCondition()

  def "OnLearningAlertCondition - use cases"() {

    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode", List.class) >>
        workingMode
    context.getEnvironment().getProperty("silenteight.scb-bridge.learning.alert.enabled", _, _) >>
        learningAlertEnabled

    when:
    def result = underTest.matches(context, null)

    then:
    result == expectedResult

    where:
    workingMode                      | learningAlertEnabled | expectedResult
    ["LEARNING", "PERIODIC_SOLVING"] | true                 | true
    ["LEARNING"]                     | true                 | true
    ["learning"]                     | true                 | true
    ["ALL"]                          | true                 | true
    ["all"]                          | true                 | true
    ["NONE"]                         | true                 | false
    ["ANOTHER_MODE"]                 | true                 | false
    []                               | true                 | false
    null                             | true                 | false
    ["LEARNING", "PERIODIC_SOLVING"] | false                | false
    ["LEARNING"]                     | false                | false
    ["learning"]                     | false                | false
    ["ALL"]                          | false                | false
    ["all"]                          | false                | false
    ["NONE"]                         | false                | false
    null                             | false                | false
  }
}
