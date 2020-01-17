package com.silenteight.sens.webapp.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeQueryRepository;
import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeRepository;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.StatusDto;
import com.silenteight.sens.webapp.backend.decisiontree.exception.DecisionTreeNotFoundException;
import com.silenteight.sens.webapp.backend.decisiontree.exception.GrpcDecisionTreeRepositoryException;

import io.grpc.StatusRuntimeException;

import java.util.List;

import static com.google.protobuf.Empty.getDefaultInstance;
import static io.grpc.Status.Code.NOT_FOUND;
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

  @Override
  public DecisionTreeDetailsDto getById(long id) {
    try {
      DecisionTreeSummary decisionTree = client
          .getDecisionTree(createGetDecisionTreeRequest(id))
          .getDecisionTree();

      log.debug("Found Decision Tree details. decisionTreeId={}", id);

      return mapToDetailsDto(decisionTree);
    } catch (StatusRuntimeException e) {
      if (e.getStatus().getCode() == NOT_FOUND) {
        log.warn("Could not find Decision Tree details. decisionTreeId={}", id, e);

        throw new DecisionTreeNotFoundException(id, e);
      } else {
        throw new GrpcDecisionTreeRepositoryException(e);
      }
    }
  }

  private static GetDecisionTreeRequest createGetDecisionTreeRequest(long id) {
    return GetDecisionTreeRequest
        .newBuilder()
        .setDecisionTreeId(id)
        .build();
  }

  private static DecisionTreeDetailsDto mapToDetailsDto(DecisionTreeSummary decisionTreeSummary) {
    return DecisionTreeDetailsDto
        .builder()
        .id(decisionTreeSummary.getId())
        .name(decisionTreeSummary.getName())
        .status(mapToStatus(decisionTreeSummary.getDecisionGroupList()))
        .build();
  }

  private enum Status {
    ACTIVE,
    INACTIVE
  }
}
