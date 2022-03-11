package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class DecisionsCollection {

  private static final String[] SYSTEM_OPERATORS = new String[] { "FFFFEED", "FSK" };

  @NonNull
  @Getter
  private final Collection<Decision> decisions;

  public int size() {
    return decisions.size();
  }

  @Nonnull
  public Optional<Instant> getLastResetDecisionDate() {
    return decisions
        .stream()
        .filter(DecisionsCollection::isResetDecision)
        .max(Comparator.comparing(Decision::createdAt))
        .map(Decision::createdAt);
  }

  private static boolean isResetDecision(Decision decision) {
    return !isAnalystDecision(decision) &&
        decision.solution() == AnalystSolution.ANALYST_NO_SOLUTION;
  }

  private static boolean isAnalystDecision(Decision decision) {
    if (decision.authorId() == null)
      return false;

    for (String systemOperator : SYSTEM_OPERATORS) {
      if (systemOperator.equalsIgnoreCase(decision.authorId()))
        return false;
    }

    return true;
  }

  @Nonnull
  Optional<AnalystSolution> getLastSolution() {
    return getLastDecision().map(Decision::solution);
  }

  @Nonnull
  Optional<Decision> getLastDecision() {
    return decisions
        .stream()
        .filter(DecisionsCollection::isAnalystDecision)
        .max(Comparator.comparing(Decision::createdAt));
  }

  boolean hasLastDecision() {
    return getLastDecision().isPresent();
  }
}
