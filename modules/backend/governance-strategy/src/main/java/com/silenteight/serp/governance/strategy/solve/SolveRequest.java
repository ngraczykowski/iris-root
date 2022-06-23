package com.silenteight.serp.governance.strategy.solve;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.solving.api.v1.AnalystSolution;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

@Value
@Builder
public class SolveRequest {

  @NonNull
  @Builder.Default
  AnalystSolution previousAnalystSolution = AnalystSolution.ANALYST_SOLUTION_UNSPECIFIED;
  @NonNull
  @Builder.Default
  Collection<FeatureVectorSolution> solvedMatchesBranchSolutions = new ArrayList<>();
  @NonNull
  @Builder.Default
  Collection<FeatureVectorSolution> unsolvedMatchesBranchSolutions = new ArrayList<>();
  @NonNull
  @Builder.Default
  Collection<FeatureVectorSolution> obsoleteMatchesBranchSolutions = new ArrayList<>();

  public boolean hasObsoleteMatchesOnly() {
    return !obsoleteMatchesBranchSolutions.isEmpty()
        && solvedMatchesBranchSolutions.isEmpty()
        && unsolvedMatchesBranchSolutions.isEmpty();
  }

  public boolean hasSolvedMatches() {
    return !solvedMatchesBranchSolutions.isEmpty();
  }

  public boolean isPreviousAnalystSolutionIn(AnalystSolution... analystSolutions) {
    return ofNullable(getPreviousAnalystSolution())
        .map(analystSolution -> isPreviousAnalystSolutionIn(analystSolution, analystSolutions))
        .orElse(FALSE);
  }

  private static boolean isPreviousAnalystSolutionIn(
      AnalystSolution analystSolution, AnalystSolution... analystSolutions) {

    return asList(analystSolutions).contains(analystSolution);
  }
}
