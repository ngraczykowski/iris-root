package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution

import spock.lang.Specification
import spock.lang.Unroll

class GnsSolutionMapperSpec extends Specification {

  def underTest = new GnsSolutionMapper(
      [
          (AnalystSolution.ANALYST_NO_SOLUTION)   : [0, 12],
          (AnalystSolution.ANALYST_FALSE_POSITIVE): [1],
          (AnalystSolution.ANALYST_TRUE_POSITIVE) : [223]
      ])

  @Unroll
  def 'should map: #type into #expectedResult'() {
    when:
    def result = underTest.mapSolution(type)

    then:
    result == expectedResult

    where:
    type | expectedResult
    0    | AnalystSolution.ANALYST_NO_SOLUTION
    1    | AnalystSolution.ANALYST_FALSE_POSITIVE
    12   | AnalystSolution.ANALYST_NO_SOLUTION
    223  | AnalystSolution.ANALYST_TRUE_POSITIVE
    404  | AnalystSolution.ANALYST_OTHER
  }
}
