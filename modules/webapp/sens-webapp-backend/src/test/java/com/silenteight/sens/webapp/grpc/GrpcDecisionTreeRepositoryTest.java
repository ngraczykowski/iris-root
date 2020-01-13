package com.silenteight.sens.webapp.grpc;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.ListDecisionTreesResponse;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.protobuf.Empty.getDefaultInstance;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcDecisionTreeRepositoryTest {

  @Mock
  private DecisionTreeGovernanceBlockingStub cilent;

  private Fixtures fixtures = new Fixtures();
  private GrpcDecisionTreeRepository underTest;

  @BeforeEach
  void setUp() {
    underTest = new GrpcDecisionTreeRepository(cilent);
  }

  @Test
  void shouldFindAllDecisionTrees() {
    // given
    when(cilent.listDecisionTrees(getDefaultInstance()))
        .thenReturn(fixtures.listDecisionTreesResponse);

    // when
    DecisionTreesDto decisionTrees = underTest.findAll();

    // then
    assertThat(decisionTrees.getTotal()).isEqualTo(3L);
    assertThat(decisionTrees.getResults())
        .extracting(DecisionTreeDto::getId)
        .containsExactly(
            fixtures.firstDecisionTree.getId(),
            fixtures.secondDecisionTree.getId(),
            fixtures.thirdDecisionTree.getId());
    assertThat(decisionTrees.getResults())
        .extracting(DecisionTreeDto::getName)
        .containsExactly(
            fixtures.firstDecisionTree.getName(),
            fixtures.secondDecisionTree.getName(),
            fixtures.thirdDecisionTree.getName());
    assertThat(decisionTrees.getResults())
        .extracting(DecisionTreeDto::getActivations)
        .containsExactly(
            fixtures.firstDecisionTree.getDecisionGroupList(),
            fixtures.secondDecisionTree.getDecisionGroupList(),
            fixtures.thirdDecisionTree.getDecisionGroupList());
  }

  private static class Fixtures {

    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final String NAME_1 = "name-1";
    private static final String NAME_2 = "name-2";
    private static final String NAME_3 = "name-3";
    private static final String ACTIVATION_1 = "activation-1";
    private static final String ACTIVATION_2 = "activation-2";
    private static final String ACTIVATION_3 = "activation-3";

    DecisionTreeSummary firstDecisionTree = DecisionTreeSummary
        .newBuilder()
        .setId(ID_1)
        .setName(NAME_1)
        .addDecisionGroup(ACTIVATION_1)
        .build();

    DecisionTreeSummary secondDecisionTree = DecisionTreeSummary
        .newBuilder()
        .setId(ID_2)
        .setName(NAME_2)
        .addDecisionGroup(ACTIVATION_2)
        .build();

    DecisionTreeSummary thirdDecisionTree = DecisionTreeSummary
        .newBuilder()
        .setId(ID_3)
        .setName(NAME_3)
        .addDecisionGroup(ACTIVATION_3)
        .build();

    ListDecisionTreesResponse listDecisionTreesResponse = ListDecisionTreesResponse
        .newBuilder()
        .addAllDecisionTree(asList(firstDecisionTree, secondDecisionTree, thirdDecisionTree))
        .build();
  }
}
