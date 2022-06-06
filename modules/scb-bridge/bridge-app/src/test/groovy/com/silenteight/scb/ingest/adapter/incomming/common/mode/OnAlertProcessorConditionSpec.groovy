package com.silenteight.scb.ingest.adapter.incomming.common.mode

import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class OnAlertProcessorConditionSpec extends Specification {

  @Subject
  def underTest = new OnAlertProcessorCondition()

  def "OnAlertProcessorCondition - use cases"() {

    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode", List.class) >>
        workingMode
    context.getEnvironment().getProperty(
        "silenteight.scb-bridge.solving.alert-processor.enabled", _, _) >> alertProcessorEnabled

    when:
    def result = underTest.matches(context, null)

    then:
    result == expectedResult

    where:
    workingMode                      | alertProcessorEnabled | expectedResult
    ["PERIODIC_SOLVING", "LEARNING"] | true                  | true
    ["PERIODIC_SOLVING"]             | true                  | true
    ["periodic_solving"]             | true                  | true
    ["ALL"]                          | true                  | true
    ["all"]                          | true                  | true
    ["NONE"]                         | true                  | false
    ["ANOTHER_MODE"]                 | true                  | false
    []                               | true                  | false
    null                             | true                  | false
    ["PERIODIC_SOLVING", "LEARNING"] | false                 | false
    ["PERIODIC_SOLVING"]             | false                 | false
    ["periodic_solving"]             | false                 | false
    ["ALL"]                          | false                 | false
    ["all"]                          | false                 | false
    ["NONE"]                         | false                 | false
    null                             | false                 | false
  }
}
