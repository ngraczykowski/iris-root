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
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode") >> workingMode
    context.getEnvironment().getProperty("silenteight.scb-bridge.solving.alert-processor.enabled", _, _) >> alertProcessorEnabled

    when:
    def result = underTest.matches(context, null)

    then:
    result == expectedResult

    where:
    workingMode      | alertProcessorEnabled | expectedResult
    "NORMAL"         | true                  | true
    "normal"         | true                  | true
    ""               | true                  | true
    null             | true                  | true
    "REAL_TIME_ONLY" | true                  | false
    "REAL_TIME_ONLY" | false                 | false
    "NORMAL"         | false                 | false
    ""               | false                 | false
    null             | false                 | false
  }
}
