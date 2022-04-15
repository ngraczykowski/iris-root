package com.silenteight.serp.governance.strategy.solve

import com.silenteight.solving.api.v1.FeatureVectorSolution

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.solving.api.v1.AnalystSolution.ANALYST_SOLUTION_UNSPECIFIED

class SolveRequestSpec extends Specification {

  def someAnalystSolution = ANALYST_SOLUTION_UNSPECIFIED
  @Shared
  def nonEmptyList = [FeatureVectorSolution.SOLUTION_FALSE_POSITIVE]

  def "hasObsoleteMatchesOnly"() {
    given:
    def solveRequest = createSolveRequest(obsoleteMatches, solvedMatches, unsolvedMatches)

    when:
    def result = solveRequest.hasObsoleteMatchesOnly()

    then:
    result == expectedResult

    where:
    obsoleteMatches | solvedMatches | unsolvedMatches | expectedResult
    []              | []            | []              | false
    []              | []            | nonEmptyList    | false
    []              | nonEmptyList  | nonEmptyList    | false
    []              | nonEmptyList  | []              | false
    nonEmptyList    | nonEmptyList  | []              | false
    nonEmptyList    | []            | nonEmptyList    | false
    nonEmptyList    | nonEmptyList  | nonEmptyList    | false
    nonEmptyList    | []            | []              | true
  }

  @Unroll
  def "should hasSolvedMatches results in #expectedResult for solvedMatches=#solvedMatches"() {
    given:
    def solveRequest = createSolveRequest([], solvedMatches, [])

    when:
    def result = solveRequest.hasSolvedMatches()

    then:
    result == expectedResult

    where:
    solvedMatches | expectedResult
    []            | false
    nonEmptyList  | true
  }

  def createSolveRequest(
      Collection obsoleteMatches,
      Collection solvedMatches,
      Collection unsolvedMatches) {

    SolveRequest
        .builder()
        .previousAnalystSolution(someAnalystSolution)
        .obsoleteMatchesBranchSolutions(obsoleteMatches)
        .solvedMatchesBranchSolutions(solvedMatches)
        .unsolvedMatchesBranchSolutions(unsolvedMatches)
        .build()
  }
}
