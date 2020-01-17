package com.silenteight.sens.webapp.grpc;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.DecisionTreeResponse;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.protobuf.Empty.getDefaultInstance;
import static com.silenteight.sens.webapp.grpc.DecisionTreeSummaryFixtures.ACTIVE;
import static com.silenteight.sens.webapp.grpc.DecisionTreeSummaryFixtures.INACTIVE;
import static com.silenteight.sens.webapp.grpc.ListDecisionTreesResponseFixtures.DECISION_TREES;
import static org.assertj.core.api.Assertions.*;
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
  void shouldFindAllDecisionTrees() {
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
        .containsExactly(INACTIVE.getDecisionGroupList(), ACTIVE.getDecisionGroupList());
  }

  @Test
  void shouldFindDecisionTreeDetails() {
    // given
    GetDecisionTreeRequest request = createGetDecisionTreeRequest(INACTIVE.getId());
    when(client.getDecisionTree(request)).thenReturn(createDecisionTreeResponse(INACTIVE));

    // when
    DecisionTreeDetailsDto details = underTest.getById(INACTIVE.getId());

    // then
    assertThat(details.getId()).isEqualTo(INACTIVE.getId());
    assertThat(details.getName()).isEqualTo(INACTIVE.getName());
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
