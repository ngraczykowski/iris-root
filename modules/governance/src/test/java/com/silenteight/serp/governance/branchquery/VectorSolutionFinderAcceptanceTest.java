package com.silenteight.serp.governance.branchquery;

import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.VectorSolution;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.serp.governance.decisiontreesummaryquery.TestInMemoryDecisionTreeQueryRepository;
import com.silenteight.serp.governance.decisiontreesummaryquery.TestInMemoryDecisionTreeQueryRepository.DecisionTreeToStore;
import com.silenteight.serp.governance.featurevector.TestInMemoryFeatureVectorRepository;
import com.silenteight.serp.governance.featurevector.TestInMemoryFeatureVectorRepository.FeatureVectorToStore;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import javax.annotation.Nonnull;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VectorSolutionFinderAcceptanceTest {

  private final Fixtures fixtures = new Fixtures();

  private TestInMemoryVectorSolutionQueryRepository vectorSolutionRepository;
  private TestInMemoryFeatureVectorRepository featureVectorRepository;
  private TestInMemoryDecisionTreeQueryRepository decisionTreeRepository;

  private VectorSolutionFinder finder;

  @BeforeEach
  void setUp() {
    vectorSolutionRepository = spy(new TestInMemoryVectorSolutionQueryRepository());
    featureVectorRepository = spy(new TestInMemoryFeatureVectorRepository());
    decisionTreeRepository = spy(new TestInMemoryDecisionTreeQueryRepository());

    finder = new VectorSolutionFinder(vectorSolutionRepository);
  }

  @Test
  void shouldReturnSolvedSolutionQuery() {
    VectorSolutionQuery query = fixtures.solvedSolutionQuery1;
    vectorSolutionRepository.store(query);

    Collection<VectorSolution> solutions = findSolutions(
        query.getDecisionGroup(), query.getVectorSignature());

    assertThat(solutions).containsExactly(fixtures.expectedSolvedSolution1);
  }

  @Test
  void shouldReturnUnsolvedSolutionQuery() {
    decisionTreeRepository.store(fixtures.decisionTree1);
    FeatureVectorToStore vector = fixtures.featureVectorToStore2;
    featureVectorRepository.store(vector);

    Collection<VectorSolution> solutions = findSolutions(
        fixtures.decisionGroup, vector.getVectorSignature());

    assertThat(solutions).containsExactly(fixtures.expectedUnsolvedSolution2);
  }

  @Test
  void shouldReturnSolvedAndUnsolvedSolutionQuery() {
    decisionTreeRepository.store(fixtures.decisionTree1);
    FeatureVectorToStore vector = fixtures.featureVectorToStore2;
    featureVectorRepository.store(vector);
    VectorSolutionQuery query = fixtures.solvedSolutionQuery1;
    vectorSolutionRepository.store(query);

    Collection<VectorSolution> solutions = findSolutions(
        fixtures.decisionGroup, vector.getVectorSignature(), query.getVectorSignature());

    assertThat(solutions).containsExactlyInAnyOrder(
        fixtures.expectedSolvedSolution1,
        fixtures.expectedUnsolvedSolution2);
  }

  @Test
  void shouldReturnUnsolvedSolutionWhenVectorSolvedInDifferentDecisionGroup() {
    decisionTreeRepository.store(fixtures.decisionTree1);
    vectorSolutionRepository.store(fixtures.solvedSolutionQuery3WithDifferentDecisionGroup);
    featureVectorRepository.store(fixtures.featureVectorToStore3);

    Collection<VectorSolution> solutions = findSolutions(
        fixtures.decisionGroup,
        fixtures.solvedSolutionQuery3WithDifferentDecisionGroup.getVectorSignature());

    assertThat(solutions).containsExactlyInAnyOrder(
        fixtures.expectedUnsolvedSolution3);
  }

  @Test
  void shouldNotSearchInSolutionRepo_whenSignaturesAreEmpty() {
    findSolutions(fixtures.decisionGroup);

    verifyNoMoreInteractions(vectorSolutionRepository);
  }

  @Test
  void shouldNotSearchInVectorsRepo_whenAllVectorsAreSolved() {
    vectorSolutionRepository.store(fixtures.solvedSolutionQuery1);

    findSolutions(
        fixtures.decisionGroup, fixtures.solvedSolutionQuery1.getVectorSignature());

    verifyNoMoreInteractions(featureVectorRepository);
  }

  @Test
  void shouldNotSearchInTreeRepo_whenAllVectorsAreSolved() {
    vectorSolutionRepository.store(fixtures.solvedSolutionQuery1);

    findSolutions(
        fixtures.decisionGroup, fixtures.solvedSolutionQuery1.getVectorSignature());

    verifyNoMoreInteractions(decisionTreeRepository);
  }

  private Collection<VectorSolution> findSolutions(String decisionGroup, ByteString... signatures) {
    return finder.findSolutions(decisionGroup, asList(signatures));
  }

  private static class Fixtures {

    String decisionGroup = "decisionGroup1";

    DecisionTreeToStore decisionTree1 = DecisionTreeToStore.builder()
        .decisionTreeId(1)
        .decisionTreeName("tree")
        .decisionGroupNames(singletonList(decisionGroup))
        .build();

    VectorSolutionQuery solvedSolutionQuery1 = createSolvedSolutionQuery(
        1, decisionGroup, decisionTree1.getDecisionTreeId());
    VectorSolutionQuery solvedSolutionQuery3WithDifferentDecisionGroup = createSolvedSolutionQuery(
        3, "someGroup", 2);

    FeatureVectorToStore featureVectorToStore2 = createFeatureVectorToStore(2L);
    FeatureVectorToStore featureVectorToStore3 = createFeatureVectorToStore(3L);

    VectorSolution expectedSolvedSolution1 = VectorSolution
        .newBuilder()
        .setSolution(solvedSolutionQuery1.getSolution())
        .setVectorSignature(solvedSolutionQuery1.getVectorSignature())
        .setReasoningBranch(
            ReasoningBranchId
                .newBuilder()
                .setDecisionTreeId(solvedSolutionQuery1.getDecisionTreeId())
                .setFeatureVectorId(solvedSolutionQuery1.getFeatureVectorId()))
        .build();

    VectorSolution expectedUnsolvedSolution2 = VectorSolution
        .newBuilder()
        .setSolution(BranchSolution.BRANCH_NO_DECISION)
        .setVectorSignature(featureVectorToStore2.getVectorSignature())
        .build();

    VectorSolution expectedUnsolvedSolution3 = VectorSolution
        .newBuilder()
        .setSolution(BranchSolution.BRANCH_NO_DECISION)
        .setVectorSignature(solvedSolutionQuery3WithDifferentDecisionGroup.getVectorSignature())
        .build();

    @Nonnull
    private static VectorSolutionQuery createSolvedSolutionQuery(
        long id, String decisionGroup, long decisionTreeId) {

      VectorSolutionQuery query = new VectorSolutionQuery();
      query.setDecisionGroup(decisionGroup);
      query.setVectorSignature(copyFromUtf8("vectorSignature" + id));
      query.setFeatureVectorId(id);
      query.setDecisionTreeId(decisionTreeId);
      query.setSolution(BranchSolution.BRANCH_NO_DECISION);
      return query;
    }

    @Nonnull
    private static FeatureVectorToStore createFeatureVectorToStore(long id) {
      return FeatureVectorToStore.builder()
          .id(id)
          .values(emptyList())
          .vectorSignature(copyFromUtf8("vectorSignature" + id))
          .featureSignature(copyFromUtf8("featureSignature"))
          .build();
    }
  }
}
