package com.silenteight.sens.webapp.grpc.decisiontree;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.api.ListDecisionTreesResponse;

import static com.silenteight.sens.webapp.grpc.decisiontree.DecisionTreeSummaryFixtures.ACTIVE;
import static com.silenteight.sens.webapp.grpc.decisiontree.DecisionTreeSummaryFixtures.INACTIVE;
import static java.util.Arrays.asList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ListDecisionTreesResponseFixtures {

  static final ListDecisionTreesResponse DECISION_TREES = ListDecisionTreesResponse
      .newBuilder()
      .addAllDecisionTree(asList(INACTIVE, ACTIVE))
      .build();
}
