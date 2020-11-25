package com.silenteight.serp.governance.branch;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.persistence.EntityNotFoundException;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_NO_DECISION;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_POTENTIAL_TRUE_POSITIVE;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceAcceptanceTest {

  private BranchService serviceUnderTest;

  @Mock
  private AuditingLogger auditingLogger;

  private InMemoryBranchRepository repo;

  private final Fixtures fixtures = new Fixtures();

  @BeforeEach
  void setUp() {
    repo = new InMemoryBranchRepository();
    var config = new BranchModuleConfiguration(auditingLogger, repo);

    serviceUnderTest = config.branchService();
  }

  @Test
  void branchDoesNotExist_updateOrCreateWithoutSolutionAndEnablement_savesWithDefaults() {
    ConfigureBranchRequest request = ConfigureBranchRequest.builder()
        .correlationId(fixtures.correlationId)
        .decisionTreeId(1L)
        .featureVectorId(1L)
        .build();

    updateOrCreate(request);

    assertFoundBranch(1L, 1L)
        .hasValueSatisfying(b -> assertThat(b.isEnabled()).isTrue())
        .hasValueSatisfying(b -> assertThat(b.getSolution()).isEqualTo(fixtures.ndSolution));
  }

  @Test
  void branchesDoNotExist_updateOrCreate_savesBranchesCorrectly() {
    ConfigureBranchRequest request1 = ConfigureBranchRequest.builder()
        .correlationId(fixtures.correlationId)
        .decisionTreeId(2L)
        .featureVectorId(1L)
        .solution(fixtures.fpSolution)
        .enabled(false)
        .build();
    ConfigureBranchRequest request2 = ConfigureBranchRequest.builder()
        .correlationId(fixtures.correlationId)
        .decisionTreeId(2L)
        .featureVectorId(2L)
        .solution(fixtures.ptpSolution)
        .enabled(true)
        .build();

    updateOrCreate(request1, request2);

    assertFoundBranch(2L, 1L)
        .hasValueSatisfying(b -> assertThat(b.getSolution()).isEqualTo(fixtures.fpSolution))
        .hasValueSatisfying(b -> assertThat(b.isEnabled()).isFalse());
    assertFoundBranch(2L, 2L)
        .hasValueSatisfying(b -> assertThat(b.getSolution()).isEqualTo(fixtures.ptpSolution))
        .hasValueSatisfying(b -> assertThat(b.isEnabled()).isTrue());
  }

  @Test
  void branchDoesNotExist_update_throwsEntityNotFoundException() {
    ConfigureBranchRequest request = ConfigureBranchRequest.builder()
        .correlationId(fixtures.correlationId)
        .decisionTreeId(1L)
        .featureVectorId(1L)
        .solution(fixtures.ptpSolution)
        .enabled(true)
        .build();

    assertThrows(EntityNotFoundException.class, () -> update(request));
  }

  @Test
  void branchExists_updateWithoutAnyChanges_branchNotChanged() {
    Branch branch = fixtures.disabledPtpBranch;
    repo.save(branch);

    update(ConfigureBranchRequest.builder()
        .correlationId(fixtures.correlationId)
        .decisionTreeId(branch.getDecisionTreeId())
        .featureVectorId(branch.getFeatureVectorId())
        .build());

    assertFoundBranch(branch.getDecisionTreeId(), branch.getFeatureVectorId())
        .hasValueSatisfying(b -> assertThat(b.getSolution()).isEqualTo(fixtures.ptpSolution))
        .hasValueSatisfying(b -> assertThat(b.isEnabled()).isFalse());
  }

  @Test
  void branchesExist_updateWithSolutionAndEnablement_branchesChangedCorrectly() {
    Branch branch1 = fixtures.enabledFpBranch;
    Branch branch2 = fixtures.disabledPtpBranch;
    repo.save(branch1);
    repo.save(branch2);

    ConfigureBranchRequest request1 = ConfigureBranchRequest.builder()
        .correlationId(fixtures.correlationId)
        .decisionTreeId(branch1.getDecisionTreeId())
        .featureVectorId(branch1.getFeatureVectorId())
        .solution(fixtures.ptpSolution)
        .enabled(false)
        .build();
    ConfigureBranchRequest request2 = ConfigureBranchRequest.builder()
        .correlationId(fixtures.correlationId)
        .decisionTreeId(branch2.getDecisionTreeId())
        .featureVectorId(branch2.getFeatureVectorId())
        .solution(fixtures.fpSolution)
        .enabled(true)
        .build();

    update(request1, request2);

    assertFoundBranch(branch1.getDecisionTreeId(), branch1.getFeatureVectorId())
        .hasValueSatisfying(b -> assertThat(b.getSolution()).isEqualTo(fixtures.ptpSolution))
        .hasValueSatisfying(b -> assertThat(b.isEnabled()).isEqualTo(false));

    assertFoundBranch(branch2.getDecisionTreeId(), branch2.getFeatureVectorId())
        .hasValueSatisfying(b -> assertThat(b.getSolution()).isEqualTo(fixtures.fpSolution))
        .hasValueSatisfying(b -> assertThat(b.isEnabled()).isEqualTo(true));
  }

  @Test
  void branchSaved_auditLogEntryAdded() {
    Branch branch = fixtures.disabledPtpBranch;
    repo.save(branch);

    update(ConfigureBranchRequest.builder()
        .correlationId(fixtures.correlationId)
        .decisionTreeId(branch.getDecisionTreeId())
        .featureVectorId(branch.getFeatureVectorId())
        .build());

    verify(auditingLogger).log(argThat((e) -> e.getCorrelationId().equals(fixtures.correlationId)));
  }

  private void updateOrCreate(ConfigureBranchRequest... requests) {
    serviceUnderTest.bulkUpdateOrCreateBranches(asList(requests));
  }

  private void update(ConfigureBranchRequest... requests) {
    serviceUnderTest.bulkUpdateBranches(asList(requests));
  }

  private OptionalAssert<Branch> assertFoundBranch(long decisionTreeId, long featureVectorId) {
    return assertThat(findBranch(decisionTreeId, featureVectorId))
        .hasValueSatisfying(b -> assertThat(b.getDecisionTreeId()).isEqualTo(decisionTreeId))
        .hasValueSatisfying(b -> assertThat(b.getFeatureVectorId()).isEqualTo(featureVectorId));
  }

  private Optional<Branch> findBranch(long decisionTreeId, long featureVectorId) {
    return repo.findByDecisionTreeIdAndFeatureVectorId(decisionTreeId, featureVectorId);
  }

  private static class Fixtures {

    UUID correlationId = UUID.randomUUID();

    BranchSolution ptpSolution = BRANCH_POTENTIAL_TRUE_POSITIVE;
    BranchSolution fpSolution = BRANCH_FALSE_POSITIVE;
    BranchSolution ndSolution = BRANCH_NO_DECISION;

    Branch disabledPtpBranch = branch(1L, 1L, ptpSolution, false);
    Branch enabledFpBranch = branch(2L, 2L, fpSolution, true);

    private static Branch branch(
        long decisionTreeId,
        long featureVectorId,
        @Nullable BranchSolution solution,
        @Nullable Boolean enabled) {

      Branch branch = new Branch();
      branch.setDecisionTreeId(decisionTreeId);
      branch.setFeatureVectorId(featureVectorId);
      ofNullable(solution).ifPresent(branch::setSolution);
      ofNullable(enabled).ifPresent(branch::setEnabled);
      return branch;
    }
  }
}
