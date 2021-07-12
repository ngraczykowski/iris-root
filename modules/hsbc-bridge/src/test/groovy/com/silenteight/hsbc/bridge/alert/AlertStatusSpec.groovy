package com.silenteight.hsbc.bridge.alert

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.hsbc.bridge.alert.AlertStatus.COMPLETED
import static com.silenteight.hsbc.bridge.alert.AlertStatus.ERROR
import static com.silenteight.hsbc.bridge.alert.AlertStatus.PRE_PROCESSED
import static com.silenteight.hsbc.bridge.alert.AlertStatus.PROCESSING
import static com.silenteight.hsbc.bridge.alert.AlertStatus.RECOMMENDATION_READY
import static com.silenteight.hsbc.bridge.alert.AlertStatus.STORED

class AlertStatusSpec extends Specification {

  @Unroll
  def 'should `#status` be internal'() {
    when:
    def result = status.isInternal()

    then:
    result

    where:
    status << [
        PRE_PROCESSED, RECOMMENDATION_READY
    ]
  }

  @Unroll
  def 'should `#status` not be treated as internal'() {
    when:
    def result = status.isInternal()

    then:
    !result

    where:
    status << [
        COMPLETED, PROCESSING, ERROR, STORED
    ]
  }
}
