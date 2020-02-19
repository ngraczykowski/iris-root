package com.silenteight.sens.webapp.grpc.decisiontree;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class DecisionTreeSummaryFixtures {

  static final DecisionTreeSummary INACTIVE = DecisionTreeSummary
      .newBuilder()
      .setId(1L)
      .setName("inactive-decision-tree")
      .build();

  static final DecisionTreeSummary ACTIVE = DecisionTreeSummary
      .newBuilder()
      .setId(2L)
      .setName("active-decision-tree")
      .addDecisionGroup("US_PERD_DENY")
      .addDecisionGroup("HK_PRVB_DUDL")
      .build();
}
