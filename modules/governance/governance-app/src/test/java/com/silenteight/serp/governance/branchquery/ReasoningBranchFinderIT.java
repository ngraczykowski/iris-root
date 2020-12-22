package com.silenteight.serp.governance.branchquery;

import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest;
import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest.BranchSolutionFilter;
import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest.DecisionTreeFilter;
import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest.EnablementFilter;
import com.silenteight.proto.serp.v1.common.Pagination;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.serp.governance.branch.BranchDbFixturesConfiguration;
import com.silenteight.serp.governance.branch.BranchDbFixturesService;
import com.silenteight.serp.governance.branch.BranchFixture;
import com.silenteight.serp.governance.branchquery.ReasoningBranchFinderIT.TestRepositoryConfiguration;
import com.silenteight.serp.governance.branchquery.dto.ListReasoningBranchDto;
import com.silenteight.serp.governance.decisiontree.DecisionTreeDbFixtureService;
import com.silenteight.serp.governance.decisiontree.DecisionTreeDbFixturesConfiguration;
import com.silenteight.serp.governance.featurevector.FeatureVectorDbFixturesConfiguration;
import com.silenteight.serp.governance.featurevector.FeatureVectorDbFixturesService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(
    classes = TestRepositoryConfiguration.class,
    initializers = PostgresTestInitializer.class)
class ReasoningBranchFinderIT extends BaseDataJpaTest {

  @Autowired
  private ReasoningBranchQueryRepository repository;
  @Autowired
  private DecisionTreeDbFixtureService decisionTreeDbFixtureService;
  @Autowired
  private FeatureVectorDbFixturesService featureVectorDbFixturesService;
  @Autowired
  private BranchDbFixturesService branchDbFixturesService;

  private ReasoningBranchFinder serviceUnderTest;

  private final Fixtures fixtures = new Fixtures();

  @BeforeEach
  void init() {
    serviceUnderTest = new ReasoningBranchConfiguration().reasoningBranchFinder(repository);
  }

  @Test
  void throwsExceptionWhenReasoningBranchNotPresent() {
    assertThatThrownBy(() -> serviceUnderTest.getByDecisionTreeIdAndFeatureVectorId(1, 2))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @Transactional
  void getByIdReturnsCorrectResult() {
    long decisionTreeId = createDefaultDecisionTree();
    long featureVectorId = featureVectorDbFixturesService.storeFeatureVector(
        fixtures.featureVectorSignature,
        fixtures.featureValues);
    branchDbFixturesService.storeBranch(BranchFixture.builder()
        .decisionTreeId(decisionTreeId)
        .featureVectorId(featureVectorId)
        .solution(fixtures.solution)
        .enabled(fixtures.enabled)
        .lastUsedAt(fixtures.lastUsedAt)
        .build());

    ReasoningBranchSummary summary = serviceUnderTest.getByDecisionTreeIdAndFeatureVectorId(
        decisionTreeId, featureVectorId);

    assertThat(summary.getReasoningBranchId())
        .satisfies(assertDecisionTreeId(decisionTreeId))
        .satisfies(assertFeatureVectorId(featureVectorId));
    assertThat(summary.getSolution()).isEqualTo(fixtures.solution);
    assertThat(summary.getFeatureValueList()).contains(fixtures.featureValues);
    assertThat(summary.getEnabled()).isEqualTo(fixtures.enabled);
    assertThat(summary.getLastUsedAt()).isEqualTo(toTimestamp(fixtures.lastUsedAt));
    assertThat(summary.getCreatedAt()).isNotNull();
    assertThat(summary.getUpdatedAt()).isNotNull();
    assertThat(summary.getFeatureVectorSignature().toStringUtf8())
        .isEqualTo(fixtures.featureVectorSignature);
  }

  @Test
  @Transactional
  void getByIdReturnsNoSolutionIfNoBranchExists() {
    long decisionTreeId = createDefaultDecisionTree();
    long featureVectorId = createDefaultFeatureVector();

    ReasoningBranchSummary summary = serviceUnderTest.getByDecisionTreeIdAndFeatureVectorId(
        decisionTreeId, featureVectorId);

    assertThat(summary.getSolution()).isEqualTo(BranchSolution.BRANCH_NO_DECISION);
  }

  @Test
  @Transactional
  void findAllSupportsRequestWithPagination() {
    long decisionTreeId = createDefaultDecisionTree();
    long featureVectorId = createDefaultFeatureVector();
    createDefaultBranch(decisionTreeId, featureVectorId);
    createDefaultBranch(decisionTreeId, featureVectorId);

    ListReasoningBranchesRequest listReasoningBranchesRequest = createRequestWithPagination(
        decisionTreeId,
        fixtures.solution,
        fixtures.enabled,
        1);
    ListReasoningBranchDto dto = serviceUnderTest.findAll(listReasoningBranchesRequest);

    assertThat(dto.getBranchSummaries().size()).isEqualTo(1);
    assertThat(dto.getTotalCount()).isEqualTo(2);
  }

  @Test
  @Transactional
  void findAllSupportsRequestWithoutPagination() {
    long decisionTreeId = createDefaultDecisionTree();
    long featureVectorId = createDefaultFeatureVector();
    createDefaultBranch(decisionTreeId, featureVectorId);
    createDefaultBranch(decisionTreeId, featureVectorId);

    ListReasoningBranchesRequest listReasoningBranchesRequest = createRequestWithoutPagination(
        decisionTreeId,
        fixtures.solution,
        fixtures.enabled);
    ListReasoningBranchDto dto = serviceUnderTest.findAll(listReasoningBranchesRequest);

    assertThat(dto.getBranchSummaries().size()).isEqualTo(2);
    assertThat(dto.getTotalCount()).isEqualTo(2);
  }

  @Test
  @Transactional
  void findAllTakesIntoAccountSolutionPredicate() {
    long decisionTreeId = createDefaultDecisionTree();
    long featureVectorId = createDefaultFeatureVector();
    branchDbFixturesService.storeBranch(BranchFixture.builder()
        .decisionTreeId(decisionTreeId)
        .featureVectorId(featureVectorId)
        .solution(BranchSolution.BRANCH_FALSE_POSITIVE)
        .enabled(fixtures.enabled)
        .lastUsedAt(fixtures.lastUsedAt)
        .build());

    ListReasoningBranchesRequest listReasoningBranchesRequest = createRequestWithPagination(
        decisionTreeId,
        BranchSolution.BRANCH_POTENTIAL_TRUE_POSITIVE,
        fixtures.enabled,
        fixtures.pageSize);
    ListReasoningBranchDto dto = serviceUnderTest.findAll(listReasoningBranchesRequest);

    assertThat(dto.getBranchSummaries()).isEmpty();
    assertThat(dto.getTotalCount()).isEqualTo(0);
  }

  @Transactional
  @Test
  void findAllTakesIntoAccountEnabledPredicate() {
    long decisionTreeId = createDefaultDecisionTree();
    long featureVectorId = createDefaultFeatureVector();
    branchDbFixturesService.storeBranch(BranchFixture.builder()
        .decisionTreeId(decisionTreeId)
        .featureVectorId(featureVectorId)
        .solution(fixtures.solution)
        .enabled(true)
        .lastUsedAt(fixtures.lastUsedAt)
        .build());

    ListReasoningBranchesRequest listReasoningBranchesRequest = createRequestWithPagination(
        decisionTreeId,
        fixtures.solution,
        false,
        fixtures.pageSize);
    ListReasoningBranchDto dto = serviceUnderTest.findAll(listReasoningBranchesRequest);

    assertThat(dto.getBranchSummaries()).isEmpty();
    assertThat(dto.getTotalCount()).isEqualTo(0);
  }

  @Transactional
  @Test
  void findAllTakesIntoAccountDecisionTreeIdPredicate() {
    long decisionTreeId = createDefaultDecisionTree();
    long featureVectorId = createDefaultFeatureVector();
    createDefaultBranch(decisionTreeId, featureVectorId);

    ListReasoningBranchesRequest listReasoningBranchesRequest = createRequestWithPagination(
        decisionTreeId + 1,
        fixtures.solution,
        fixtures.enabled,
        fixtures.pageSize);
    ListReasoningBranchDto dto = serviceUnderTest.findAll(listReasoningBranchesRequest);

    assertThat(dto.getBranchSummaries()).isEmpty();
    assertThat(dto.getTotalCount()).isEqualTo(0);
  }

  @Nonnull
  private static ListReasoningBranchesRequest createRequestWithPagination(
      long decisionTreeId,
      BranchSolution branchSolution, boolean enabled, int pageSize) {
    return ListReasoningBranchesRequest.newBuilder()
        .setDecisionTreeFilter(DecisionTreeFilter.newBuilder()
            .addDecisionTreeIds(decisionTreeId)
            .build())
        .setBranchSolutionFilter(BranchSolutionFilter.newBuilder()
            .addSolutions(branchSolution)
            .build())
        .setEnablementFilter(EnablementFilter.newBuilder()
            .setEnabled(enabled)
            .build())
        .setPagination(Pagination.newBuilder()
            .setPageIndex(0)
            .setPageSize(pageSize)
            .build())
        .build();
  }

  @Nonnull
  private static ListReasoningBranchesRequest createRequestWithoutPagination(
      long decisionTreeId,
      BranchSolution branchSolution, boolean enabled) {
    return ListReasoningBranchesRequest.newBuilder()
        .setDecisionTreeFilter(DecisionTreeFilter.newBuilder()
            .addDecisionTreeIds(decisionTreeId)
            .build())
        .setBranchSolutionFilter(BranchSolutionFilter.newBuilder()
            .addSolutions(branchSolution)
            .build())
        .setEnablementFilter(EnablementFilter.newBuilder()
            .setEnabled(enabled)
            .build())
        .build();
  }

  @Nonnull
  private static Consumer<ReasoningBranchId> assertDecisionTreeId(long decisionTreeId) {
    return r -> assertThat(r.getDecisionTreeId()).isEqualTo(decisionTreeId);
  }

  @Nonnull
  private static Consumer<ReasoningBranchId> assertFeatureVectorId(long featureVectorId) {
    return r -> assertThat(r.getFeatureVectorId()).isEqualTo(featureVectorId);
  }

  private long createDefaultDecisionTree() {
    return decisionTreeDbFixtureService.storeDefaultBranch();
  }

  private long createDefaultFeatureVector() {
    return featureVectorDbFixturesService.storeFeatureVector(
        fixtures.featureVectorSignature,
        fixtures.featureValues);
  }

  private long createDefaultBranch(long decisionTreeId, long featureVectorId) {
    return branchDbFixturesService.storeBranch(BranchFixture.builder()
        .decisionTreeId(decisionTreeId)
        .featureVectorId(featureVectorId)
        .solution(fixtures.solution)
        .enabled(fixtures.enabled)
        .lastUsedAt(fixtures.lastUsedAt)
        .build());
  }


  @Configuration
  @ComponentScan(basePackageClasses = { BranchQueryModule.class })
  @Import({
      DecisionTreeDbFixturesConfiguration.class,
      FeatureVectorDbFixturesConfiguration.class,
      BranchDbFixturesConfiguration.class
  })
  static class TestRepositoryConfiguration {
  }

  private static class Fixtures {

    OffsetDateTime lastUsedAt =
        OffsetDateTime.of(2020, 7, 15, 10, 22, 0, 0, ZoneOffset.UTC);

    String[] featureValues = { "NO_MATCH", "EXACT_MATCH" };
    String featureVectorSignature = "R7hwLUC4NgsUieoNkA8aYM1eFZQ=";
    BranchSolution solution = BranchSolution.BRANCH_FALSE_POSITIVE;
    Boolean enabled = true;
    int pageSize = 10;
  }
}