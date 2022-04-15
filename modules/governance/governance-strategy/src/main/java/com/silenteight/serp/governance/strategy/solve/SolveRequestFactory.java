package com.silenteight.serp.governance.strategy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.Match;
import com.silenteight.solving.api.v1.MatchFlag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.silenteight.solving.api.v1.AnalystSolution.ANALYST_SOLUTION_UNSPECIFIED;
import static com.silenteight.solving.api.v1.MatchFlag.OBSOLETE;
import static com.silenteight.solving.api.v1.MatchFlag.SOLVED;

@RequiredArgsConstructor
class SolveRequestFactory {

  @NonNull
  private final Alert alert;

  SolveRequest create() {
    Set<FeatureVectorSolution> solvedMatchSolutions = new HashSet<>();
    Set<FeatureVectorSolution> unsolvedMatchSolutions = new HashSet<>();
    Set<FeatureVectorSolution> obsoleteMatchSolutions = new HashSet<>();

    for (Match match : alert.getMatchesList()) {
      List<MatchFlag> flags = match.getFlagsList();
      FeatureVectorSolution solution = match.getSolution();

      if (isObsolete(flags))
        obsoleteMatchSolutions.add(solution);
      if (isNotObsoleteAndSolvedByAnalyst(flags))
        solvedMatchSolutions.add(solution);
      if (isNotObsoleteAndUnsolvedByAnalyst(flags))
        unsolvedMatchSolutions.add(solution);
    }

    return SolveRequest
        .builder()
        // TODO(mmastylo): for now Alert does not expose analyst solution
        .previousAnalystSolution(ANALYST_SOLUTION_UNSPECIFIED)
        .obsoleteMatchesBranchSolutions(obsoleteMatchSolutions)
        .solvedMatchesBranchSolutions(solvedMatchSolutions)
        .unsolvedMatchesBranchSolutions(unsolvedMatchSolutions)
        .build();
  }

  private static boolean isObsolete(List<MatchFlag> flags) {
    return flags.contains(OBSOLETE);
  }

  private static boolean isSolvedByAnalyst(List<MatchFlag> flags) {
    return flags.contains(SOLVED);
  }

  private static boolean isNotObsoleteAndSolvedByAnalyst(List<MatchFlag> flags) {
    return !isObsolete(flags) && isSolvedByAnalyst(flags);
  }

  private static boolean isNotObsoleteAndUnsolvedByAnalyst(List<MatchFlag> flags) {
    return !(isObsolete(flags) || isSolvedByAnalyst(flags));
  }
}
