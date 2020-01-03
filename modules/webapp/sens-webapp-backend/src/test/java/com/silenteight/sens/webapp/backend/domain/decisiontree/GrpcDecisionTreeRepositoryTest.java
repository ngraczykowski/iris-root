package com.silenteight.sens.webapp.backend.domain.decisiontree;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc.DecisionTreeGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.ListDecisionTreesResponse;
import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;
import com.silenteight.proto.serp.v1.governance.ModelSummary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.google.protobuf.Empty.getDefaultInstance;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcDecisionTreeRepositoryTest {

  @Mock
  private DecisionTreeGovernanceBlockingStub stub;

  private Fixtures fixtures = new Fixtures();
  private GrpcDecisionTreeRepository underTest;

  @BeforeEach
  void setUp() {
    underTest = new GrpcDecisionTreeRepository(stub);
  }

  @Test
  void shouldFindAllDecisionTrees() {
    // given
    when(stub.listDecisionTrees(getDefaultInstance()))
        .thenReturn(fixtures.listDecisionTreesResponse);

    // when
    List<DecisionTreeView> decisionTrees = underTest.findAll();

    // then
    assertThat(decisionTrees).hasSize(2);
    assertThat(decisionTrees).extracting(DecisionTreeView::getId).containsExactlyInAnyOrder(
        fixtures.firstDecisionTree.getId(), fixtures.secondDecisionTree.getId());
    assertThat(decisionTrees).extracting(DecisionTreeView::getName).containsExactlyInAnyOrder(
        fixtures.firstDecisionTree.getName(), fixtures.secondDecisionTree.getName());
  }

  private static class Fixtures {

    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final String NAME_1 = "name-1";
    private static final String NAME_2 = "name-2";
    private static final String MODEL_NAME = "model-name";

    DecisionTreeSummary firstDecisionTree = DecisionTreeSummary
        .newBuilder()
        .setId(ID_1)
        .setName(NAME_1)
        .setModelSummary(createModelSummary())
        .build();

    DecisionTreeSummary secondDecisionTree = DecisionTreeSummary
        .newBuilder()
        .setId(ID_2)
        .setName(NAME_2)
        .setModelSummary(createModelSummary())
        .build();

    ListDecisionTreesResponse listDecisionTreesResponse = ListDecisionTreesResponse
        .newBuilder()
        .addAllDecisionTree(asList(firstDecisionTree, secondDecisionTree))
        .build();

    private static ModelSummary createModelSummary() {
      return ModelSummary
          .newBuilder()
          .setName(MODEL_NAME)
          .build();
    }
  }
}
