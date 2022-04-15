package com.silenteight.serp.governance.strategy.solve

import com.silenteight.solving.api.v1.FeatureVectorSolution

import spock.lang.Specification
import spock.lang.Unroll

import static com.silenteight.serp.governance.strategy.solve.RecommendedAction.*
import static com.silenteight.solving.api.v1.FeatureVectorSolution.*

class MatchSolutionsReducerSpec extends Specification {

  def objectUnderTest = new MatchSolutionsReducer()

  def 'should throw illegal argument exception when no solutions'() {
    when:
    objectUnderTest.reduce([])

    then:
    thrown(IllegalArgumentException)
  }

  def 'should throw exception when illegal set of solution has been provided'() {
    when:
    objectUnderTest.reduce([FeatureVectorSolution.FEATURE_VECTOR_SOLUTION_UNSPECIFIED])

    then:
    thrown(IllegalArgumentException)
  }

  @Unroll
  def 'should reduce solutions=#solutions into #expectedResult'() {
    when:
    def result = objectUnderTest.reduce(solutions)

    then:
    result == expectedResult

    where:
    solutions                                       | expectedResult
    [SOLUTION_POTENTIAL_TRUE_POSITIVE]              | ACTION_POTENTIAL_TRUE_POSITIVE
    [SOLUTION_HINTED_POTENTIAL_TRUE_POSITIVE]       | ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE
    [SOLUTION_NO_DECISION, SOLUTION_FALSE_POSITIVE] | ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE
    [SOLUTION_NO_DECISION]                          | ACTION_INVESTIGATE
    [SOLUTION_FALSE_POSITIVE]                       | ACTION_FALSE_POSITIVE
    [SOLUTION_HINTED_FALSE_POSITIVE]                | ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE
  }
}
