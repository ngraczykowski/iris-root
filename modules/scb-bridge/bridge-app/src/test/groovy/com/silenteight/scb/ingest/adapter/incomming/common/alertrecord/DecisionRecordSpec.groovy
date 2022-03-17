package com.silenteight.scb.ingest.adapter.incomming.common.alertrecord

import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution

import spock.lang.Specification
import spock.lang.Unroll

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
    decisionRecord('', AnalystSolution.ANALYST_NO_SOLUTION) | true
    decisionRecord('a', AnalystSolution.ANALYST_NO_SOLUTION)          | false
    decisionRecord('FSK', AnalystSolution.ANALYST_NO_SOLUTION)        | true
    decisionRecord('FFFFEED', AnalystSolution.ANALYST_NO_SOLUTION)    | true
    decisionRecord('', AnalystSolution.ANALYST_FALSE_POSITIVE)        | false
    decisionRecord('a', AnalystSolution.ANALYST_FALSE_POSITIVE)       | false
    decisionRecord('FSK', AnalystSolution.ANALYST_FALSE_POSITIVE)     | false
    decisionRecord('FFFFEED', AnalystSolution.ANALYST_FALSE_POSITIVE) | false
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
