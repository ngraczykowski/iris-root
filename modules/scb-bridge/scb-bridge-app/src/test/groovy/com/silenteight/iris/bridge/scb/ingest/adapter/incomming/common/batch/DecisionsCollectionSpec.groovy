/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch

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
    com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision.builder()
        .authorId(authorId)
        .build()
  }
}
