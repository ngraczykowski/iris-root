package com.silenteight.scb.ingest.adapter.incomming.common.mode

import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class OnOracleConditionSpec extends Specification {

  @Subject
  def underTest = new OnOracleCondition()

  def "OnOracleCondition - use cases"() {

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
    workingMode                               | expectedResult
    ["REAL_TIME_SOLVING", "PERIODIC_SOLVING"] | true
    ["REAL_TIME_SOLVING", "LEARNING"]         | true
    ["PERIODIC_SOLVING", "LEARNING"]          | true
    ["LEARNING"]                              | true
    ["PERIODIC_SOLVING"]                      | true
    ["ALL"]                                   | true
    ["all"]                                   | true
    ["REAL_TIME_SOLVING"]                     | false
    ["NONE"]                                  | false
    ["ANOTHER_MODE"]                          | false
    []                                        | false
    null                                      | false
  }
}
