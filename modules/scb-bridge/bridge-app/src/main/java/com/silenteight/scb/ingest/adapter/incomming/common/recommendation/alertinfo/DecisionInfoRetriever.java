package com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo;

import com.silenteight.proto.serp.scb.v1.ScbDecisionDetails;
import com.silenteight.proto.serp.v1.alert.AnalystSolution;
import com.silenteight.proto.serp.v1.alert.Decision;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import com.google.protobuf.Any;
import com.google.protobuf.util.Timestamps;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

class DecisionInfoRetriever {

  static Optional<String> getLastIntermediateStateName(List<Decision> decisions) {
    if (decisions.isEmpty())
      return Optional.empty();

    return decisions.stream()
        .filter(DecisionInfoRetriever::hasSolutionAndDetails)
        .filter(DecisionInfoRetriever::isIntermediateState)
        .sorted(Comparator.comparing(Decision::getCreatedAt, Timestamps.comparator()).reversed())
        .map(d -> getScbStateName(d.getDetails()))
        .findFirst();
  }

  private static boolean isIntermediateState(
      com.silenteight.proto.serp.v1.alert.Decision decision) {
    return decision.getSolution() == AnalystSolution.ANALYST_OTHER;
  }

  private static boolean hasSolutionAndDetails(
      com.silenteight.proto.serp.v1.alert.Decision decision) {
    return nonNull(decision.getSolution()) && decision.hasDetails();
  }

  private static String getScbStateName(Any decisionDetails) {
    return AnyUtils.maybeUnpack(decisionDetails, ScbDecisionDetails.class)
        .map(ScbDecisionDetails::getStateName)
        .orElseThrow(() -> new IllegalStateException("Invalid decision details"));
  }
}
