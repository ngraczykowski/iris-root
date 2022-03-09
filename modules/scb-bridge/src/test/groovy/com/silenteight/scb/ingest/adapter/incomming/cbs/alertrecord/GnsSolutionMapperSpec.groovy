package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.*

class GnsSolutionMapperSpec extends Specification {

  def underTest = new GnsSolutionMapper(
      [
          (ANALYST_NO_SOLUTION)   : [0, 12],
          (ANALYST_FALSE_POSITIVE): [1],
          (ANALYST_TRUE_POSITIVE) : [223]
      ])

  @Unroll
  def 'should map: #type into #expectedResult'() {
    when:
    def result = underTest.mapSolution(type)

    then:
    result == expectedResult

    where:
    type | expectedResult
    0    | ANALYST_NO_SOLUTION
    1    | ANALYST_FALSE_POSITIVE
    12   | ANALYST_NO_SOLUTION
    223  | ANALYST_TRUE_POSITIVE
    404  | ANALYST_OTHER
  }
}
