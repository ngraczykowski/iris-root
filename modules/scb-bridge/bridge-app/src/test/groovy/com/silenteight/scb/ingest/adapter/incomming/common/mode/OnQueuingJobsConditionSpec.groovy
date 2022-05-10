package com.silenteight.scb.ingest.adapter.incomming.common.mode

import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import spock.lang.Specification
import spock.lang.Subject

class OnQueuingJobsConditionSpec extends Specification {

  @Subject
  def underTest = new OnQueuingJobsCondition()

  def "OnQueuingJobsCondition - use cases"() {

    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode") >> workingMode

    when:
    def result = underTest.matches(context, null)

    then:
    result == expectedResult

    where:
    workingMode      | expectedResult
    "NORMAL"         | true
    "normal"         | true
    ""               | true
    null             | true
    "REAL_TIME_ONLY" | false
  }
}
