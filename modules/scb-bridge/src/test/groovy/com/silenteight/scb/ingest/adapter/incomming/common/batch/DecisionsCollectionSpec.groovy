package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.proto.serp.v1.alert.Decision

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
    decisions                       | expectedResult
    []                              | false
    [Decision.newBuilder().build()] | true
    [createDecision('FSK')]         | false
    [createDecision('FFFFEED')]     | false
    [createDecision('test')]        | true
  }

  def createDecision(String authorId) {
    Decision.newBuilder()
        .setAuthorId(authorId)
        .build()
  }
}
