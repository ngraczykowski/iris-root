package com.silenteight.customerbridge.common.batch;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.alert.AnalystSolution;
import com.silenteight.proto.serp.v1.alert.Decision;
import com.silenteight.protocol.utils.MoreTimestamps;

import com.google.protobuf.util.Timestamps;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import javax.annotation.Nonnull;

import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_NO_SOLUTION;

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
        .map(Decision::getCreatedAt)
        .max(Timestamps.comparator())
        .map(MoreTimestamps::toInstant);
  }

  @Nonnull
  Optional<Decision> getLastDecision() {
    return decisions
        .stream()
        .filter(DecisionsCollection::isAnalystDecision)
        .max(Comparator.comparing(Decision::getCreatedAt, Timestamps.comparator()));
  }

  @Nonnull
  Optional<AnalystSolution> getLastSolution() {
    return getLastDecision().map(Decision::getSolution);
  }

  private static boolean isAnalystDecision(Decision decision) {
    if (decision.getAuthorId() == null)
      return false;

    for (String systemOperator : SYSTEM_OPERATORS) {
      if (systemOperator.equalsIgnoreCase(decision.getAuthorId()))
        return false;
    }

    return true;
  }

  private static boolean isResetDecision(Decision decision) {
    return !isAnalystDecision(decision) && decision.getSolution() == ANALYST_NO_SOLUTION;
  }

  boolean hasLastDecision() {
    return getLastDecision().isPresent();
  }
}
