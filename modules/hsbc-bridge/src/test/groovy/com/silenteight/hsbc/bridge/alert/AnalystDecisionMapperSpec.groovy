package com.silenteight.hsbc.bridge.alert

import spock.lang.Specification
import spock.lang.Unroll

class AnalystDecisionMapperSpec extends Specification {

  @Unroll
  def "should map analyst_decision into #expectedResult when value=`#value"() {
    given:
    def underTest = new AnalystDecisionMapper()

    when:
    def result = underTest.getAnalystDecision(value)

    then:
    result == expectedResult

    where:
    value                                    || expectedResult
    'False Positive'                         || 'analyst_decision_false_positive'

    'True Match Exit Completed'              || 'analyst_decision_true_positive'
    'True Match Retained'                    || 'analyst_decision_true_positive'
    'PEP True Match'                         || 'analyst_decision_true_positive'
    'True Match Exit / Retained Recommended' || 'analyst_decision_true_positive'

    'Level 1 Review'                         || 'analyst_decision_pending'
    'Level 2 Review'                         || 'analyst_decision_pending'
    'Level 3 Review'                         || 'analyst_decision_pending'
    'Isolated Level 3'                       || 'analyst_decision_pending'
    'Isolated Level 1 / 2'                   || 'analyst_decision_pending'

    ''                                       || 'analyst_decision_unknown'
    null                                     || 'analyst_decision_unknown'
  }
}
