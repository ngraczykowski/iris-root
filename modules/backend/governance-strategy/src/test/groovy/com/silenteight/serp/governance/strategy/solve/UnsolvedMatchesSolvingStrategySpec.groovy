package com.silenteight.serp.governance.strategy.solve

class UnsolvedMatchesSolvingStrategySpec extends BaseSolvingStrategySpec {

  @Override
  SolvingStrategy getStrategy() {
    SolvingStrategyType.UNSOLVED_MATCHES.getStrategy()
  }

  def "obsolete matches only -> false positive"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches | solvedMatches | unsolvedMatches | expectedAiSolution
    AS_TC           | [BS_FP, BS_FP]  | []            | []              | R_FP
    AS_NONE         | [BS_FP]         | []            | []              | R_FP
    AS_NONE         | [BS_PTP]        | []            | []              | R_FP
    AS_PTP          | [BS_PTP, BS_FP] | []            | []              | R_FP
    AS_PTP          | [BS_ND]         | []            | []              | R_FP
    AS_FP           | [BS_PTP]        | []            | []              | R_FP
  }

  def "should use only unsolved matches"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches   | solvedMatches   | unsolvedMatches | expectedAiSolution
    AS_PTP          | []                | []              | [BS_PTP]        | R_PTP
    AS_TC           | []                | []              | [BS_FP]         | R_FP
    AS_TC           | []                | []              | [BS_ND]         | R_ND
    AS_TC           | [BS_PTP]          | [BS_ND, BS_FP]  | [BS_FP]         | R_FP
    AS_TC           | []                | []              | [BS_PTP]        | R_PTP
    AS_PTP          | [BS_PTP]          | [BS_PTP]        | [BS_FP, BS_ND]  | R_PF
    AS_PTP          | [BS_FP]           | [BS_FP]         | [BS_FP, BS_FP]  | R_FP
    AS_PTP          | [BS_PTP, AS_NONE] | [BS_FP]         | [BS_FP]         | R_FP
    AS_FP           | []                | []              | [BS_PTP]        | R_PTP
    AS_FP           | []                | []              | [BS_FP]         | R_FP
    AS_FP           | []                | []              | [BS_ND]         | R_ND
    AS_FP           | [BS_FP]           | [BS_FP, BS_PTP] | [BS_ND, BS_FP]  | R_PF
    AS_FP           | [BS_PTP]          | [BS_ND, BS_FP]  | [BS_FP]         | R_FP
    AS_FP           | []                | []              | [BS_PTP]        | R_PTP
    AS_FP           | [BS_PTP]          | [BS_PTP]        | [BS_FP, BS_ND]  | R_PF
    AS_O            | []                | []              | [BS_PTP]        | R_PTP
    AS_O            | []                | []              | [BS_FP]         | R_FP
    AS_O            | []                | []              | [BS_ND]         | R_ND
    AS_O            | [BS_FP]           | [BS_FP, BS_PTP] | [BS_ND, BS_FP]  | R_PF
    AS_O            | [BS_PTP]          | [BS_ND, BS_FP]  | [BS_FP]         | R_FP
    AS_O            | []                | []              | [BS_PTP]        | R_PTP
    AS_O            | [BS_PTP]          | [BS_PTP]        | [BS_FP, BS_ND]  | R_PF
  }
}
