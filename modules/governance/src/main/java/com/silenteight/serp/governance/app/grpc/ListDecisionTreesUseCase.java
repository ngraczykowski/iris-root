package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.ListDecisionTreesResponse;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.serp.governance.decisiontreesummaryquery.DecisionTreeSummaryFinder;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@RequiredArgsConstructor
class ListDecisionTreesUseCase {

  private final DecisionTreeSummaryFinder decisionTreeFinder;

  ListDecisionTreesResponse activate() {
    Collection<DecisionTreeSummary> trees = decisionTreeFinder.getAll();

    return mapToListResponse(trees);
  }

  @NotNull
  private static ListDecisionTreesResponse mapToListResponse(
      Collection<DecisionTreeSummary> trees) {

    return ListDecisionTreesResponse.newBuilder().addAllDecisionTree(trees).build();
  }
}
