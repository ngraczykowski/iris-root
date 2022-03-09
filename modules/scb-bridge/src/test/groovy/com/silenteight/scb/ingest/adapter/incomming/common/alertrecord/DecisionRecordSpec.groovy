package com.silenteight.scb.ingest.adapter.incomming.common.alertrecord

import com.silenteight.proto.serp.v1.alert.AnalystSolution

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_FALSE_POSITIVE
import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_NO_SOLUTION

class DecisionRecordSpec extends Specification {

  @Unroll
  def 'should return analyst decision = #expectedResult when #decisionRecord'() {
    when:
    def result = decisionRecord.isAnalystDecision()

    then:
    result == expectedResult

    where:
    decisionRecord            | expectedResult
    decisionRecord('')        | false
    decisionRecord('a')       | true
    decisionRecord('12345')   | true
    decisionRecord('FSK')     | false
    decisionRecord('FFFFEED') | false
  }

  @Unroll
  def 'should return reset decision = #expectedResult when #decisionRecord'() {
    when:
    def result = decisionRecord.isResetDecision()

    then:
    result == expectedResult

    where:
    decisionRecord                                    | expectedResult
    decisionRecord('', ANALYST_NO_SOLUTION)           | true
    decisionRecord('a', ANALYST_NO_SOLUTION)          | false
    decisionRecord('FSK', ANALYST_NO_SOLUTION)        | true
    decisionRecord('FFFFEED', ANALYST_NO_SOLUTION)    | true
    decisionRecord('', ANALYST_FALSE_POSITIVE)        | false
    decisionRecord('a', ANALYST_FALSE_POSITIVE)       | false
    decisionRecord('FSK', ANALYST_FALSE_POSITIVE)     | false
    decisionRecord('FFFFEED', ANALYST_FALSE_POSITIVE) | false
  }

  def decisionRecord(String operator) {
    DecisionRecord.builder()
        .operator(operator)
        .systemId('')
        .build()
  }

  def decisionRecord(String operator, AnalystSolution solution) {
    DecisionRecord.builder()
        .operator(operator)
        .systemId('')
        .solution(solution)
        .build()
  }
}
