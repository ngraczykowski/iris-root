package com.silenteight.serp.governance.strategy.solve

class AllMatchesSolvingStrategySpec extends BaseSolvingStrategySpec {

  @Override
  SolvingStrategy getStrategy() {
    SolvingStrategyType.ALL_MATCHES.getStrategy()
  }

  def "obsolete matches only -> false positive"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedRecommendation)

    where:
    analystSolution | obsoleteMatches | solvedMatches | unsolvedMatches | expectedRecommendation
    AS_TC           | [BS_FP, BS_FP]  | []            | []              | R_FP
    AS_NONE         | [BS_FP]         | []            | []              | R_FP
    AS_NONE         | [BS_PTP]        | []            | []              | R_FP
    AS_PTP          | [BS_PTP, BS_FP] | []            | []              | R_FP
    AS_PTP          | [BS_ND]         | []            | []              | R_FP
    AS_FP           | [BS_PTP]        | []            | []              | R_FP
  }

  def "solved and unsolved matches -> reduce all"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedRecommendation)

    where:
    analystSolution | obsoleteMatches | solvedMatches    | unsolvedMatches | expectedRecommendation
    AS_NONE         | []              | [BS_FP]          | []              | R_FP
    AS_NONE         | []              | [BS_ND]          | []              | R_ND
    AS_NONE         | []              | [BS_PTP]         | []              | R_PTP
    AS_FP           | []              | [BS_FP]          | []              | R_FP
    AS_FP           | []              | [BS_ND]          | []              | R_ND
    AS_FP           | []              | [BS_PTP]         | []              | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_FP]          | []              | R_FP
    AS_NONE         | [BS_PTP]        | [BS_ND]          | []              | R_ND
    AS_NONE         | [BS_PTP]        | [BS_PTP]         | []              | R_PTP
    AS_FP           | [BS_PTP]        | [BS_FP]          | []              | R_FP
    AS_FP           | [BS_PTP]        | [BS_ND]          | []              | R_ND
    AS_FP           | [BS_PTP]        | [BS_PTP]         | []              | R_PTP
    AS_NONE         | []              | [BS_FP, BS_FP]   | []              | R_FP
    AS_NONE         | []              | [BS_FP, BS_ND]   | []              | R_PF
    AS_NONE         | []              | [BS_FP, BS_PTP]  | []              | R_PTP
    AS_NONE         | []              | [BS_ND, BS_ND]   | []              | R_ND
    AS_NONE         | []              | [BS_PTP, BS_PTP] | []              | R_PTP
    AS_NONE         | []              | [BS_PTP, BS_ND]  | []              | R_PTP
    AS_FP           | []              | [BS_FP, BS_FP]   | []              | R_FP
    AS_FP           | []              | [BS_FP, BS_ND]   | []              | R_PF
    AS_FP           | []              | [BS_FP, BS_PTP]  | []              | R_PTP
    AS_FP           | []              | [BS_ND, BS_ND]   | []              | R_ND
    AS_FP           | []              | [BS_PTP, BS_PTP] | []              | R_PTP
    AS_FP           | []              | [BS_PTP, BS_ND]  | []              | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_FP]   | []              | R_FP
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_ND]   | []              | R_PF
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_PTP]  | []              | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_ND, BS_ND]   | []              | R_ND
    AS_NONE         | [BS_PTP]        | [BS_PTP, BS_PTP] | []              | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_PTP, BS_ND]  | []              | R_PTP
    AS_FP           | [BS_PTP]        | [BS_FP, BS_FP]   | []              | R_FP
    AS_FP           | [BS_PTP]        | [BS_FP, BS_ND]   | []              | R_PF
    AS_FP           | [BS_PTP]        | [BS_FP, BS_PTP]  | []              | R_PTP
    AS_FP           | [BS_PTP]        | [BS_ND, BS_ND]   | []              | R_ND
    AS_FP           | [BS_PTP]        | [BS_PTP, BS_PTP] | []              | R_PTP
    AS_FP           | [BS_PTP]        | [BS_PTP, BS_ND]  | []              | R_PTP
    AS_NONE         | []              | [BS_FP, BS_FP]   | [BS_FP]         | R_FP
    AS_NONE         | []              | [BS_FP, BS_ND]   | [BS_FP]         | R_PF
    AS_NONE         | []              | [BS_FP, BS_PTP]  | [BS_FP]         | R_PTP
    AS_NONE         | []              | [BS_ND, BS_ND]   | [BS_FP]         | R_PF
    AS_NONE         | []              | [BS_PTP, BS_PTP] | [BS_FP]         | R_PTP
    AS_NONE         | []              | [BS_PTP, BS_ND]  | [BS_FP]         | R_PTP
    AS_FP           | []              | [BS_FP, BS_FP]   | [BS_FP]         | R_FP
    AS_FP           | []              | [BS_FP, BS_ND]   | [BS_FP]         | R_PF
    AS_FP           | []              | [BS_FP, BS_PTP]  | [BS_FP]         | R_PTP
    AS_FP           | []              | [BS_ND, BS_ND]   | [BS_FP]         | R_PF
    AS_FP           | []              | [BS_PTP, BS_PTP] | [BS_FP]         | R_PTP
    AS_FP           | []              | [BS_PTP, BS_ND]  | [BS_FP]         | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_FP]   | [BS_FP]         | R_FP
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_ND]   | [BS_FP]         | R_PF
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_PTP]  | [BS_FP]         | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_ND, BS_ND]   | [BS_FP]         | R_PF
    AS_NONE         | [BS_PTP]        | [BS_PTP, BS_PTP] | [BS_FP]         | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_PTP, BS_ND]  | [BS_FP]         | R_PTP
    AS_FP           | [BS_PTP]        | [BS_FP, BS_FP]   | [BS_FP]         | R_FP
    AS_FP           | [BS_PTP]        | [BS_FP, BS_ND]   | [BS_FP]         | R_PF
    AS_FP           | [BS_PTP]        | [BS_FP, BS_PTP]  | [BS_FP]         | R_PTP
    AS_FP           | [BS_PTP]        | [BS_ND, BS_ND]   | [BS_FP]         | R_PF
    AS_FP           | [BS_PTP]        | [BS_PTP, BS_PTP] | [BS_FP]         | R_PTP
    AS_FP           | [BS_PTP]        | [BS_PTP, BS_ND]  | [BS_FP]         | R_PTP
    AS_NONE         | []              | [BS_FP, BS_FP]   | [BS_ND]         | R_PF
    AS_NONE         | []              | [BS_FP, BS_ND]   | [BS_ND]         | R_PF
    AS_NONE         | []              | [BS_FP, BS_PTP]  | [BS_ND]         | R_PTP
    AS_NONE         | []              | [BS_ND, BS_ND]   | [BS_ND]         | R_ND
    AS_NONE         | []              | [BS_PTP, BS_PTP] | [BS_ND]         | R_PTP
    AS_NONE         | []              | [BS_PTP, BS_ND]  | [BS_ND]         | R_PTP
    AS_FP           | []              | [BS_FP, BS_FP]   | [BS_ND]         | R_PF
    AS_FP           | []              | [BS_FP, BS_ND]   | [BS_ND]         | R_PF
    AS_FP           | []              | [BS_FP, BS_PTP]  | [BS_ND]         | R_PTP
    AS_FP           | []              | [BS_ND, BS_ND]   | [BS_ND]         | R_ND
    AS_FP           | []              | [BS_PTP, BS_PTP] | [BS_ND]         | R_PTP
    AS_FP           | []              | [BS_PTP, BS_ND]  | [BS_ND]         | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_FP]   | [BS_ND]         | R_PF
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_ND]   | [BS_ND]         | R_PF
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_PTP]  | [BS_ND]         | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_ND, BS_ND]   | [BS_ND]         | R_ND
    AS_NONE         | [BS_PTP]        | [BS_PTP, BS_PTP] | [BS_ND]         | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_PTP, BS_ND]  | [BS_ND]         | R_PTP
    AS_FP           | [BS_PTP]        | [BS_FP, BS_FP]   | [BS_ND]         | R_PF
    AS_FP           | [BS_PTP]        | [BS_FP, BS_ND]   | [BS_ND]         | R_PF
    AS_FP           | [BS_PTP]        | [BS_FP, BS_PTP]  | [BS_ND]         | R_PTP
    AS_FP           | [BS_PTP]        | [BS_ND, BS_ND]   | [BS_ND]         | R_ND
    AS_FP           | [BS_PTP]        | [BS_PTP, BS_PTP] | [BS_ND]         | R_PTP
    AS_FP           | [BS_PTP]        | [BS_PTP, BS_ND]  | [BS_ND]         | R_PTP
    AS_NONE         | []              | [BS_FP, BS_FP]   | [BS_PTP]        | R_PTP
    AS_NONE         | []              | [BS_FP, BS_ND]   | [BS_PTP]        | R_PTP
    AS_NONE         | []              | [BS_FP, BS_PTP]  | [BS_PTP]        | R_PTP
    AS_NONE         | []              | [BS_ND, BS_ND]   | [BS_PTP]        | R_PTP
    AS_NONE         | []              | [BS_PTP, BS_PTP] | [BS_PTP]        | R_PTP
    AS_NONE         | []              | [BS_PTP, BS_ND]  | [BS_PTP]        | R_PTP
    AS_FP           | []              | [BS_FP, BS_FP]   | [BS_PTP]        | R_PTP
    AS_FP           | []              | [BS_FP, BS_ND]   | [BS_PTP]        | R_PTP
    AS_FP           | []              | [BS_FP, BS_PTP]  | [BS_PTP]        | R_PTP
    AS_FP           | []              | [BS_ND, BS_ND]   | [BS_PTP]        | R_PTP
    AS_FP           | []              | [BS_PTP, BS_PTP] | [BS_PTP]        | R_PTP
    AS_FP           | []              | [BS_PTP, BS_ND]  | [BS_PTP]        | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_FP]   | [BS_PTP]        | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_ND]   | [BS_PTP]        | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_FP, BS_PTP]  | [BS_PTP]        | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_ND, BS_ND]   | [BS_PTP]        | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_PTP, BS_PTP] | [BS_PTP]        | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_PTP, BS_ND]  | [BS_PTP]        | R_PTP
    AS_FP           | [BS_PTP]        | [BS_FP, BS_FP]   | [BS_PTP]        | R_PTP
    AS_FP           | [BS_PTP]        | [BS_FP, BS_ND]   | [BS_PTP]        | R_PTP
    AS_FP           | [BS_PTP]        | [BS_FP, BS_PTP]  | [BS_PTP]        | R_PTP
    AS_FP           | [BS_PTP]        | [BS_ND, BS_ND]   | [BS_PTP]        | R_PTP
    AS_FP           | [BS_PTP]        | [BS_PTP, BS_PTP] | [BS_PTP]        | R_PTP
    AS_FP           | [BS_PTP]        | [BS_PTP, BS_ND]  | [BS_PTP]        | R_PTP
  }
}
