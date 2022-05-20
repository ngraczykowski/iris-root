package com.silenteight.hsbc.bridge.alert

import spock.lang.Specification
import spock.lang.Unroll

class AnalystDecisionMapperSpec extends Specification {

  def static TRUE_POSITIVE = 'analyst_decision_true_positive'
  def static FALSE_POSITIVE = 'analyst_decision_false_positive'
  def static PENDING = 'analyst_decision_pending'
  def static UNKNOWN = 'analyst_decision_unknown'

  @Unroll
  def "should map analyst_decision into #expectedResult when value=`#value"() {
    given:
    def underTest = new AnalystDecisionMapper(decisionMap)

    when:
    def result = underTest.getAnalystDecision(value)

    then:
    result == expectedResult

    where:
    value                                    || expectedResult
    'False Positive'                         || FALSE_POSITIVE

    'True Match Exit Completed'              || TRUE_POSITIVE
    'True Match Retained'                    || TRUE_POSITIVE
    'PEP True Match'                         || TRUE_POSITIVE
    'True Match Exit / Retained Recommended' || TRUE_POSITIVE

    'Level 1 Review'                         || PENDING
    'Level 2 Review'                         || PENDING
    'Level 3 Review'                         || PENDING
    'Isolated Level 3'                       || PENDING
    'Isolated Level 1 / 2'                   || PENDING

    ''                                       || UNKNOWN
    null                                     || UNKNOWN
  }

  def decisionMap = ['True Match Exit Completed'             : TRUE_POSITIVE,
                     'True Match Retained'                   : TRUE_POSITIVE,
                     'PEP True Match'                        : TRUE_POSITIVE,
                     'True Match Exit / Retained Recommended': TRUE_POSITIVE,
                     'False Positive'                        : FALSE_POSITIVE,
                     'Level 1 Review'                        : PENDING,
                     'Level 2 Review'                        : PENDING,
                     'Level 3 Review'                        : PENDING,
                     'Isolated Level 3'                      : PENDING,
                     'Isolated Level 1 / 2'                  : PENDING,
                     "UNKNOWN"                               : UNKNOWN]
}
