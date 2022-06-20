/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision

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
    decisionRecord('', Decision.AnalystSolution.ANALYST_NO_SOLUTION)           | true
    decisionRecord('a', Decision.AnalystSolution.ANALYST_NO_SOLUTION)          | false
    decisionRecord('FSK', Decision.AnalystSolution.ANALYST_NO_SOLUTION)        | true
    decisionRecord('FFFFEED', Decision.AnalystSolution.ANALYST_NO_SOLUTION)    | true
    decisionRecord('', Decision.AnalystSolution.ANALYST_FALSE_POSITIVE)        | false
    decisionRecord('a', Decision.AnalystSolution.ANALYST_FALSE_POSITIVE)       | false
    decisionRecord('FSK', Decision.AnalystSolution.ANALYST_FALSE_POSITIVE)     | false
    decisionRecord('FFFFEED', Decision.AnalystSolution.ANALYST_FALSE_POSITIVE) | false
  }

  def decisionRecord(String operator) {
    DecisionRecord.builder()
        .operator(operator)
        .systemId('')
        .build()
  }

  def decisionRecord(String operator, Decision.AnalystSolution solution) {
    DecisionRecord.builder()
        .operator(operator)
        .systemId('')
        .solution(solution)
        .build()
  }
}
