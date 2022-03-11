package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision

import spock.lang.Specification
import spock.lang.Unroll

class DecisionsCollectionSpec extends Specification {

  @Unroll
  def "should hasLastDecision = #expectedResult for #decisions"() {
    when:
    def result = new DecisionsCollection(decisions).hasLastDecision()

    then:
    result == expectedResult

    where:
    decisions                   | expectedResult
    []                          | false
    [createDecision('')]        | true
    [createDecision('FSK')]     | false
    [createDecision('FFFFEED')] | false
    [createDecision('test')]    | true
  }

  def createDecision(String authorId) {
    Decision.builder()
        .authorId(authorId)
        .build()
  }
}
