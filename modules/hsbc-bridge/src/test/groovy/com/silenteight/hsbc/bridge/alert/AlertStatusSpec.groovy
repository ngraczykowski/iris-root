package com.silenteight.hsbc.bridge.alert

import spock.lang.Specification
import spock.lang.Unroll

class AlertStatusSpec extends Specification {

  @Unroll
  def 'should `#status` be internal'() {
    when:
    def result = status.isInternal()

    then:
    result

    where:
    status << [
        AlertStatus.PRE_PROCESSED, AlertStatus.RECOMMENDATION_READY
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
        AlertStatus.COMPLETED, AlertStatus.PROCESSING, AlertStatus.ERROR, AlertStatus.STORED
    ]
  }
}
