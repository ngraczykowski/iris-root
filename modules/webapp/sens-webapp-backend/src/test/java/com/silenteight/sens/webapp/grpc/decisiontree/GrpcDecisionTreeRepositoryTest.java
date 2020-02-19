package com.silenteight.sens.webapp.grpc.decisiontree;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.DecisionTreeResponse;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.sens.webapp.backend.decisiontree.DecisionTreeDtoFixtures;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionGroupDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.backend.decisiontree.exception.DecisionTreeNotFoundException;
import com.silenteight.sens.webapp.backend.decisiontree.exception.DecisionTreeRepositoryException;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.google.protobuf.Empty.getDefaultInstance;
import static com.silenteight.sens.webapp.grpc.decisiontree.DecisionTreeSummaryFixtures.ACTIVE;
import static com.silenteight.sens.webapp.grpc.decisiontree.DecisionTreeSummaryFixtures.INACTIVE;
import static com.silenteight.sens.webapp.grpc.decisiontree.ListDecisionTreesResponseFixtures.DECISION_TREES;
import static io.grpc.Status.Code.ABORTED;
import static io.grpc.Status.Code.NOT_FOUND;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcDecisionTreeRepositoryTest {

  @Mock
  private DecisionTreeGovernanceBlockingStub client;

  private GrpcDecisionTreeRepository underTest;

  @BeforeEach
  void setUp() {
    underTest = new GrpcDecisionTreeRepository(client);
  }

  @Test
  void decisionTreesDtoWhenDecisionTreesAvailable() {
    // given
    when(client.listDecisionTrees(getDefaultInstance())).thenReturn(DECISION_TREES);

    // when
    DecisionTreesDto decisionTrees = underTest.findAll();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(2L);
    assertThat(decisionTrees.getResults())
        .extracting(DecisionTreeDto::getId)
        .containsExactly(INACTIVE.getId(), ACTIVE.getId());
    assertThat(decisionTrees.getResults())
        .extracting(DecisionTreeDto::getName)
        .containsExactly(INACTIVE.getName(), ACTIVE.getName());
    assertThat(decisionTrees.getResults())
        .extracting(DecisionTreeDto::getActivations)
        .extracting(GrpcDecisionTreeRepositoryTest::extractDecisionGroupNames)
        .containsExactly(INACTIVE.getDecisionGroupList(), ACTIVE.getDecisionGroupList());
  }

  @Test
  void decisionTreeDetailsWhenDecisionTreeAvailable() {
    // given
    GetDecisionTreeRequest request = createGetDecisionTreeRequest(ACTIVE.getId());
    when(client.getDecisionTree(request)).thenReturn(createDecisionTreeResponse(ACTIVE));

    // when
    DecisionTreeDetailsDto details = underTest.getById(ACTIVE.getId());

    // then
    assertThat(details.getId()).isEqualTo(ACTIVE.getId());
    assertThat(details.getName()).isEqualTo(ACTIVE.getName());
    assertThat(details.getActivations())
        .extracting(DecisionGroupDto::getName)
        .isEqualTo(ACTIVE.getDecisionGroupList());
  }

  @Test
  void throwDecisionTreeNotFoundExceptionWhenDecisionTreeNotAvailable() {
    // given
    GetDecisionTreeRequest request = createGetDecisionTreeRequest(ACTIVE.getId());
    when(client.getDecisionTree(request))
        .thenThrow(new StatusRuntimeException(Status.fromCode(NOT_FOUND)));

    // when, then
    assertThrows(
        DecisionTreeNotFoundException.class,
        () -> underTest.getById(DecisionTreeDtoFixtures.ACTIVE.getId()));
  }

  @Test
  void throwDecisionTreeRepositoryExceptionWhenRequestAborted() {
    // given
    GetDecisionTreeRequest request = createGetDecisionTreeRequest(ACTIVE.getId());
    when(client.getDecisionTree(request))
        .thenThrow(new StatusRuntimeException(Status.fromCode(ABORTED)));

    // when, then
    assertThrows(
        DecisionTreeRepositoryException.class,
        () -> underTest.getById(DecisionTreeDtoFixtures.ACTIVE.getId()));
  }

  private static List<String> extractDecisionGroupNames(List<DecisionGroupDto> decisionGroups) {
    return decisionGroups
        .stream()
        .map(DecisionGroupDto::getName)
        .collect(toList());
  }

  private static GetDecisionTreeRequest createGetDecisionTreeRequest(long id) {
    return GetDecisionTreeRequest
        .newBuilder()
        .setDecisionTreeId(id)
        .build();
  }

  private static DecisionTreeResponse createDecisionTreeResponse(
      DecisionTreeSummary decisionTree) {

    return DecisionTreeResponse
        .newBuilder()
        .setDecisionTree(decisionTree)
        .build();
  }
}
