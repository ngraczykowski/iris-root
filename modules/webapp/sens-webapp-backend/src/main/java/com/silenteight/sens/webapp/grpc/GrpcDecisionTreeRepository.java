package com.silenteight.sens.webapp.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeQueryRepository;
import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeRepository;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.StatusDto;

import java.util.List;

import static com.google.protobuf.Empty.getDefaultInstance;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class GrpcDecisionTreeRepository implements
    DecisionTreeQueryRepository, DecisionTreeRepository {

  private final DecisionTreeGovernanceBlockingStub client;

  @Override
  public DecisionTreesDto findAll() {
    List<DecisionTreeDto> decisionTrees = client
        .listDecisionTrees(getDefaultInstance())
        .getDecisionTreeList()
        .stream()
        .map(GrpcDecisionTreeRepository::mapToDto)
        .collect(toList());

    log.debug("Found {} Decision Trees", decisionTrees.size());

    return new DecisionTreesDto(decisionTrees);
  }

  private static DecisionTreeDto mapToDto(DecisionTreeSummary decisionTreeSummary) {
    return DecisionTreeDto
        .builder()
        .id(decisionTreeSummary.getId())
        .name(decisionTreeSummary.getName())
        .status(mapToStatus(decisionTreeSummary.getDecisionGroupList()))
        .activations(decisionTreeSummary.getDecisionGroupList())
        .build();
  }

  private static StatusDto mapToStatus(List<String> activations) {
    return StatusDto
        .builder()
        .name(mapToStatusName(activations))
        .build();
  }

  private static String mapToStatusName(List<String> activations) {
    return activations.isEmpty() ? Status.INACTIVE.name() : Status.ACTIVE.name();
  }

  private enum Status {
    ACTIVE,
    INACTIVE
  }
}
