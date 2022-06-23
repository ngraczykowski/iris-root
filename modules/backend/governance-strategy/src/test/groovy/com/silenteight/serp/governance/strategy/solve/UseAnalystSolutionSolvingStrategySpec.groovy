package com.silenteight.serp.governance.strategy.solve

class UseAnalystSolutionSolvingStrategySpec extends BaseSolvingStrategySpec {

  @Override
  SolvingStrategy getStrategy() {
    SolvingStrategyType.USE_ANALYST_SOLUTION.getStrategy()
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
    AS_O            | [BS_PTP]        | []            | []              | R_FP
  }

  def "TP/PTP/O previous analyst solution (and matches other than only obsolete) -> PTP if at lest one unsolved match is PTP"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches | solvedMatches | unsolvedMatches        | expectedAiSolution
    AS_PTP          | []              | []            | [BS_PTP]               | R_PTP
    AS_TC           | []              | []            | [BS_PTP]               | R_PTP
    AS_O            | []              | []            | [BS_PTP]               | R_PTP
    AS_PTP          | []              | []            | [BS_PTP, BS_FP]        | R_PTP
    AS_TC           | []              | []            | [BS_PTP, BS_FP]        | R_PTP
    AS_O            | []              | []            | [BS_PTP, BS_FP]        | R_PTP
    AS_PTP          | []              | []            | [BS_PTP, BS_ND]        | R_PTP
    AS_TC           | []              | []            | [BS_PTP, BS_ND]        | R_PTP
    AS_O            | []              | []            | [BS_PTP, BS_ND]        | R_PTP
    AS_PTP          | []              | []            | [BS_PTP, BS_ND, BS_FP] | R_PTP
    AS_TC           | []              | []            | [BS_PTP, BS_ND, BS_FP] | R_PTP
    AS_O            | []              | []            | [BS_PTP, BS_ND, BS_FP] | R_PTP
  }

  def "TP/PTP/O previous analyst solution (and matches other than only obsolete) -> reduce unsolved but force FP solution to be PF"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches | solvedMatches   | unsolvedMatches | expectedAiSolution
    AS_PTP          | []              | []              | [BS_FP]         | R_PF
    AS_TC           | []              | []              | [BS_FP]         | R_PF
    AS_O            | []              | []              | [BS_FP]         | R_PF
    AS_PTP          | []              | []              | [BS_ND]         | R_ND
    AS_TC           | []              | []              | [BS_ND]         | R_ND
    AS_O            | []              | []              | [BS_ND]         | R_ND
    AS_PTP          | []              | [BS_FP]         | []              | R_ND
    AS_TC           | []              | [BS_FP]         | []              | R_ND
    AS_O            | []              | [BS_FP]         | []              | R_ND
    AS_PTP          | [BS_FP]         | [BS_FP, BS_PTP] | []              | R_ND
    AS_TC           | [BS_FP]         | [BS_FP, BS_PTP] | []              | R_ND
    AS_O            | [BS_FP]         | [BS_FP, BS_PTP] | []              | R_ND
    AS_PTP          | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]         | R_PF
    AS_TC           | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]         | R_PF
    AS_O            | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]         | R_PF
    AS_PTP          | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]  | R_PF
    AS_TC           | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]  | R_PF
    AS_O            | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]  | R_PF
    AS_PTP          | [BS_FP]         | [BS_FP]         | [BS_FP, BS_FP]  | R_PF
    AS_TC           | [BS_FP]         | [BS_FP]         | [BS_FP, BS_FP]  | R_PF
    AS_O            | [BS_FP]         | [BS_FP]         | [BS_FP, BS_FP]  | R_PF
    AS_PTP          | [BS_PTP, BS_U]  | [BS_FP]         | [BS_FP]         | R_PF
    AS_TC           | [BS_PTP, BS_U]  | [BS_FP]         | [BS_FP]         | R_PF
    AS_O            | [BS_PTP, BS_U]  | [BS_FP]         | [BS_FP]         | R_PF
    AS_PTP          | [BS_PTP, BS_ND] | [BS_PTP]        | []              | R_ND
    AS_TC           | [BS_PTP, BS_ND] | [BS_PTP]        | []              | R_ND
    AS_O            | [BS_PTP, BS_ND] | [BS_PTP]        | []              | R_ND
  }

  def "TP/PTP/O previous analyst solution (and matches other than only obsolete) with Hinted -> reduce unsolved but force FP solution to be PF"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches | solvedMatches   | unsolvedMatches         |
        expectedAiSolution
    AS_PTP          | []              | []              | [BS_PTP]                | R_PTP
    AS_TC           | []              | []              | [BS_PTP]                | R_PTP
    AS_PTP          | []              | []              | [BS_HPTP]               | R_HPTP
    AS_TC           | []              | []              | [BS_HPTP]               | R_HPTP
    AS_PTP          | []              | []              | [BS_HPTP, BS_ND]        | R_HPTP
    AS_TC           | []              | []              | [BS_HPTP, BS_ND]        | R_HPTP
    AS_PTP          | []              | []              | [BS_HPTP, BS_FP]        | R_HPTP
    AS_TC           | []              | []              | [BS_HPTP, BS_FP]        | R_HPTP
    AS_PTP          | []              | []              | [BS_HPTP, BS_FP, BS_ND] | R_HPTP
    AS_TC           | []              | []              | [BS_HPTP, BS_FP, BS_ND] | R_HPTP
    AS_PTP          | []              | []              | [BS_FP]                 | R_PF
    AS_TC           | []              | []              | [BS_FP]                 | R_PF
    AS_PTP          | []              | []              | [BS_HFP]                | R_PF
    AS_TC           | []              | []              | [BS_HFP]                | R_PF
    AS_PTP          | []              | []              | [BS_ND]                 | R_ND
    AS_TC           | []              | []              | [BS_ND]                 | R_ND
    AS_PTP          | []              | [BS_FP]         | []                      | R_ND
    AS_TC           | []              | [BS_FP]         | []                      | R_ND
    AS_PTP          | [BS_FP]         | [BS_FP, BS_PTP] | []                      | R_ND
    AS_TC           | [BS_FP]         | [BS_FP, BS_PTP] | []                      | R_ND
    AS_PTP          | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]                 | R_PF
    AS_TC           | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]                 | R_PF
    AS_PTP          | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_HFP]                | R_PF
    AS_TC           | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_HFP]                | R_PF
    AS_PTP          | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_HFP, BS_HFP]        | R_PF
    AS_TC           | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_HFP, BS_HFP]        | R_PF
    AS_PTP          | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]          | R_PF
    AS_TC           | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]          | R_PF
    AS_PTP          | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_HFP, BS_ND]  | R_PF
    AS_TC           | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_HFP, BS_ND]  | R_PF
    AS_PTP          | [BS_FP]         | [BS_FP]         | [BS_FP, BS_FP]          | R_PF
    AS_TC           | [BS_FP]         | [BS_FP]         | [BS_FP, BS_FP]          | R_PF
    AS_PTP          | [BS_FP]         | [BS_FP]         | [BS_HFP, BS_FP]         | R_PF
    AS_TC           | [BS_FP]         | [BS_FP]         | [BS_HFP, BS_FP]         | R_PF
    AS_PTP          | [BS_PTP, BS_U]  | [BS_FP]         | [BS_FP]                 | R_PF
    AS_TC           | [BS_PTP, BS_U]  | [BS_FP]         | [BS_FP]                 | R_PF
    AS_PTP          | [BS_PTP, BS_ND] | [BS_PTP]        | []                      | R_ND
    AS_TC           | [BS_PTP, BS_ND] | [BS_PTP]        | []                      | R_ND
  }

  def "Some unsolved matches and analyst solution FP -> reduce unsolved"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches | solvedMatches   | unsolvedMatches  | expectedAiSolution
    AS_FP           | []              | []              | [BS_PTP]         | R_PTP
    AS_FP           | []              | []              | [BS_FP]          | R_FP
    AS_FP           | []              | []              | [BS_HFP]         | R_HFP
    AS_FP           | []              | []              | [BS_ND]          | R_ND
    AS_FP           | [BS_FP]         | [BS_FP, BS_PTP] | [BS_ND, BS_FP]   | R_PF
    AS_FP           | [BS_FP]         | [BS_FP, BS_PTP] | [BS_ND, BS_HFP]  | R_PF
    AS_FP           | [BS_FP]         | [BS_FP, BS_PTP] | [BS_FP, BS_HFP]  | R_HFP
    AS_FP           | [BS_FP]         | [BS_FP, BS_PTP] | [BS_FP, BS_FP]   | R_FP
    AS_FP           | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]          | R_FP
    AS_FP           | []              | []              | [BS_PTP]         | R_PTP
    AS_FP           | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]   | R_PF
    AS_FP           | [BS_PTP]        | [BS_PTP]        | [BS_HFP, BS_ND]  | R_PF
    AS_FP           | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_HFP]  | R_HFP
    AS_FP           | [BS_PTP]        | [BS_PTP]        | [BS_HFP, BS_HFP] | R_HFP
  }

  def "Some unsolved matches and analyst solution not available -> reduce unsolved"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches | solvedMatches   | unsolvedMatches  | expectedAiSolution
    AS_NONE         | []              | []              | [BS_PTP]         | R_PTP
    AS_NONE         | []              | []              | [BS_FP]          | R_FP
    AS_NONE         | []              | []              | [BS_HFP]         | R_HFP
    AS_NONE         | []              | []              | [BS_ND]          | R_ND
    AS_NONE         | [BS_FP]         | [BS_FP, BS_PTP] | [BS_ND, BS_FP]   | R_PF
    AS_NONE         | [BS_FP]         | [BS_FP, BS_PTP] | [BS_ND, BS_HFP]  | R_PF
    AS_NONE         | [BS_FP]         | [BS_FP, BS_PTP] | [BS_FP, BS_HFP]  | R_HFP
    AS_NONE         | [BS_FP]         | [BS_FP, BS_PTP] | [BS_FP, BS_FP]   | R_FP
    AS_NONE         | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]          | R_FP
    AS_NONE         | []              | []              | [BS_PTP]         | R_PTP
    AS_NONE         | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]   | R_PF
    AS_NONE         | [BS_PTP]        | [BS_PTP]        | [BS_HFP, BS_ND]  | R_PF
    AS_NONE         | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_HFP]  | R_HFP
    AS_NONE         | [BS_PTP]        | [BS_PTP]        | [BS_HFP, BS_HFP] | R_HFP
  }

  def "Some unsolved matches and analyst solution is OTHER -> reduce unsolved but force FP solution to be PF"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches | solvedMatches   | unsolvedMatches  | expectedAiSolution
    AS_O            | []              | []              | [BS_PTP]         | R_PTP
    AS_O            | []              | []              | [BS_FP]          | R_PF
    AS_O            | []              | []              | [BS_HFP]         | R_PF
    AS_O            | []              | []              | [BS_ND]          | R_ND
    AS_O            | [BS_FP]         | [BS_FP, BS_PTP] | [BS_ND, BS_FP]   | R_PF
    AS_O            | [BS_FP]         | [BS_FP, BS_PTP] | [BS_ND, BS_HFP]  | R_PF
    AS_O            | [BS_FP]         | [BS_FP, BS_PTP] | [BS_FP, BS_HFP]  | R_PF
    AS_O            | [BS_FP]         | [BS_FP, BS_PTP] | [BS_FP, BS_FP]   | R_PF
    AS_O            | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]          | R_PF
    AS_O            | []              | []              | [BS_PTP]         | R_PTP
    AS_O            | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]   | R_PF
    AS_O            | [BS_PTP]        | [BS_PTP]        | [BS_HFP, BS_ND]  | R_PF
    AS_O            | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_HFP]  | R_PF
    AS_O            | [BS_PTP]        | [BS_PTP]        | [BS_HFP, BS_HFP] | R_PF
    AS_O            | []              | []              | [BS_PTP]         | R_PTP
    AS_O            | []              | []              | [BS_FP]          | R_PF
    AS_O            | []              | []              | [BS_ND]          | R_ND
    AS_O            | [BS_FP]         | [BS_FP, BS_PTP] | [BS_ND, BS_FP]   | R_PF
    AS_O            | [BS_PTP]        | [BS_ND, BS_FP]  | [BS_FP]          | R_PF
    AS_O            | []              | []              | [BS_PTP]         | R_PTP
    AS_O            | [BS_PTP]        | [BS_PTP]        | [BS_FP, BS_ND]   | R_PF
  }

  def "No unsolved matches -> ND"() {
    expect:
    solve(analystSolution, obsoleteMatches, solvedMatches, unsolvedMatches, expectedAiSolution)

    where:
    analystSolution | obsoleteMatches | solvedMatches   | unsolvedMatches | expectedAiSolution
    AS_FP           | []              | [BS_FP]         | []              | R_ND
    AS_NONE         | [BS_PTP]        | [BS_FP]         | []              | R_ND
    AS_FP           | []              | [BS_FP, BS_ND]  | []              | R_ND
    AS_NONE         | [BS_FP]         | [BS_FP, BS_PTP] | []              | R_ND
    AS_NONE         | [BS_PTP]        | [BS_ND, BS_FP]  | []              | R_ND
    AS_FP           | []              | [BS_ND]         | []              | R_ND
    AS_FP           | []              | [BS_FP]         | []              | R_ND
    AS_PTP          | [BS_PTP]        | [BS_PTP]        | []              | R_ND
    AS_TC           | [BS_PTP]        | [BS_PTP]        | []              | R_ND
    AS_O            | [BS_PTP]        | [BS_PTP]        | []              | R_ND
    AS_O            | [BS_FP]         | [BS_PTP]        | []              | R_ND
    AS_O            | [BS_FP]         | [BS_FP]         | []              | R_ND
  }
}
