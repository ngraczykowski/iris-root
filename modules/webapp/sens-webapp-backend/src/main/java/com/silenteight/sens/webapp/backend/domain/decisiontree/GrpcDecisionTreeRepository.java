package com.silenteight.sens.webapp.backend.domain.decisiontree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;

import java.util.List;

import static com.google.protobuf.Empty.getDefaultInstance;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class GrpcDecisionTreeRepository implements DecisionTreeRepository {

  private final DecisionTreeGovernanceBlockingStub client;

  @Override
  public List<DecisionTreeView> findAll() {
    List<DecisionTreeView> decisionTrees = client
        .listDecisionTrees(getDefaultInstance())
        .getDecisionTreeList()
        .stream()
        .map(GrpcDecisionTreeRepository::mapToView)
        .collect(toList());

    log.debug("Found {} Decision Trees", decisionTrees.size());

    return decisionTrees;
  }

  private static DecisionTreeView mapToView(DecisionTreeSummary decisionTreeSummary) {
    return DecisionTreeView
        .builder()
        .id(decisionTreeSummary.getId())
        .name(decisionTreeSummary.getName())
        .activations(decisionTreeSummary.getDecisionGroupList())
        .build();
  }
}
