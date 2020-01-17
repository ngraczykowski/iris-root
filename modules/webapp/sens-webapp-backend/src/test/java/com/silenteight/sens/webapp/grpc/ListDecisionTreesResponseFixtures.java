package com.silenteight.sens.webapp.grpc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.api.ListDecisionTreesResponse;

import static com.silenteight.sens.webapp.grpc.DecisionTreeSummaryFixtures.ACTIVE;
import static com.silenteight.sens.webapp.grpc.DecisionTreeSummaryFixtures.INACTIVE;
import static java.util.Arrays.asList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ListDecisionTreesResponseFixtures {

  public static final ListDecisionTreesResponse DECISION_TREES = ListDecisionTreesResponse
      .newBuilder()
      .addAllDecisionTree(asList(INACTIVE, ACTIVE))
      .build();
}
