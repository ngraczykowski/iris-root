/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision

import spock.lang.Specification
import spock.lang.Unroll

class GnsSolutionMapperSpec extends Specification {

  def underTest = new GnsSolutionMapper(
      [
          (Decision.AnalystSolution.ANALYST_NO_SOLUTION)   : [0, 12],
          (Decision.AnalystSolution.ANALYST_FALSE_POSITIVE): [1],
          (Decision.AnalystSolution.ANALYST_TRUE_POSITIVE) : [223]
      ])

  @Unroll
  def 'should map: #type into #expectedResult'() {
    when:
    def result = underTest.mapSolution(type)

    then:
    result == expectedResult

    where:
    type | expectedResult
    0    | Decision.AnalystSolution.ANALYST_NO_SOLUTION
    1    | Decision.AnalystSolution.ANALYST_FALSE_POSITIVE
    12   | Decision.AnalystSolution.ANALYST_NO_SOLUTION
    223  | Decision.AnalystSolution.ANALYST_TRUE_POSITIVE
    404  | Decision.AnalystSolution.ANALYST_OTHER
  }
}
