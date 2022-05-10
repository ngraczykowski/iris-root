package com.silenteight.scb.ingest.adapter.incomming.common.mode

import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import spock.lang.Specification

class WorkingModeUtilsSpec extends Specification {

  def "IsOnlyRealTimeModeEnabled - use cases"() {
    given:
    def context = Mock(ConditionContext)
    def environment = Mock(Environment)
    context.getEnvironment() >> environment
    context.getEnvironment().getProperty("silenteight.scb-bridge.working-mode") >> workingMode

    when:
    def result = WorkingModeUtils.isOnlyRealTimeModeEnabled(context)

    then:
    result == expectedResult

    where:
    workingMode      | expectedResult
    "REAL_TIME_ONLY" | true
    "real_time_only" | true
    "NORMAL"         | false
    "normal"         | false
    ""               | false
    null             | false
    "ANOTHER_MODE"   | false
  }
}
