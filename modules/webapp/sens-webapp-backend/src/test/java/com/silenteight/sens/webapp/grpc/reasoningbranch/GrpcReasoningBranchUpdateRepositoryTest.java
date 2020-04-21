package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchChange;
import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.ChangeBranchesRequest;
import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.sens.webapp.grpc.reasoningbranch.BranchChangeAssert.assertThatBranchChange;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.ChangeBranchesRequestAssert.assertThatChangeBranchRequest;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchUpdateRepositoryTest.ReasoningBranchUpdateRepositoryUpdateFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class GrpcReasoningBranchUpdateRepositoryTest {

  @Mock
  private BranchGovernanceBlockingStub governanceBlockingStub;

  @Mock
  private AuditLog auditLog;

  private GrpcReasoningBranchUpdateRepository underTest;

  @Captor
  private ArgumentCaptor<ChangeBranchesRequest> requestCaptor;

  @BeforeEach
  void setUp() {
    underTest = new GrpcReasoningBranchUpdateRepository(
        new BranchSolutionMapper(), governanceBlockingStub, auditLog);
  }

  @Test
  void unknownSolutionValue_throwsAiSolutionNotSupportedException() {
    Try<Void> actual = underTest.save(BRANCH_WITH_UNKNOWN_SOLUTION_CHANGED);

    assertThat(actual.getCause()).isInstanceOf(AiSolutionNotSupportedException.class);
  }

  @Test
  void nonExistingBranch_throwsBranchNotFound() {
    given(governanceBlockingStub.changeBranches(NON_EXISTING_BRANCH.getRequest()))
        .willThrow(NOT_FOUND_STATUS_EXCEPTION);

    Try<Void> actual = underTest.save(NON_EXISTING_BRANCH);

    assertThat(actual.getCause()).isInstanceOf(BranchNotFoundException.class);
  }

  @Test
  void branchWithStatusChanged_successAndGrpcRequestWithStatusChangeOnly() {
    Try<Void> actual = underTest.save(BRANCH_WITH_STATUS_CHANGED);

    assertSuccess(actual);

    verify(governanceBlockingStub).changeBranches(requestCaptor.capture());
    ChangeBranchesRequest actualRequest = requestCaptor.getValue();
    assertThatChangeBranchRequest(actualRequest)
        .hasStatusChange(BRANCH_WITH_STATUS_CHANGED.getNewStatus().orElseThrow())
        .hasBranchId(BRANCH_WITH_STATUS_CHANGED.getBranchIds().get(0));
  }

  @Test
  void twoBranches_successAndGrpcRequestWithStatusChangeOnly() {
    Try<Void> actual = underTest.save(TWO_BRANCHES_WITH_ALL_CHANGES);

    assertSuccess(actual);

    verify(governanceBlockingStub).changeBranches(requestCaptor.capture());
    ChangeBranchesRequest actualRequest = requestCaptor.getValue();

    final List<BranchChange> branchChangeList = actualRequest.getBranchChangeList();
    assertThat(branchChangeList).hasSize(2);
    final BranchChange firstChange = branchChangeList.get(0);
    assertThat(firstChange)
        .satisfies(branchChange -> assertThatBranchChange(branchChange)
            .hasBranchId(TWO_BRANCHES_WITH_ALL_CHANGES.getBranchIds().get(0))
            .hasAiSolutionChange(
                TWO_BRANCHES_WITH_ALL_CHANGES.getNewAiSolution().orElseThrow())
            .hasEnablementChange(
                TWO_BRANCHES_WITH_ALL_CHANGES.getNewStatus().orElseThrow()));

    final BranchChange secondChange = branchChangeList.get(1);
    assertThat(secondChange)
        .satisfies(branchChange -> assertThatBranchChange(branchChange)
            .hasBranchId(TWO_BRANCHES_WITH_ALL_CHANGES.getBranchIds().get(1))
            .hasAiSolutionChange(
                TWO_BRANCHES_WITH_ALL_CHANGES.getNewAiSolution().orElseThrow())
            .hasEnablementChange(
                TWO_BRANCHES_WITH_ALL_CHANGES.getNewStatus().orElseThrow()));
  }


  private static void assertSuccess(Try<Void> actual) {
    assertThat(actual.isSuccess()).isTrue();
  }

  @Test
  void branchWithAiSolutionChanged_successAndGrpcRequestWithAiSolutionChangeOnly() {
    Try<Void> actual = underTest.save(BRANCH_WITH_AI_SOLUTION_CHANGED);

    assertSuccess(actual);

    verify(governanceBlockingStub).changeBranches(requestCaptor.capture());
    ChangeBranchesRequest actualRequest = requestCaptor.getValue();
    assertThatChangeBranchRequest(actualRequest)
        .hasSolutionChange(BRANCH_WITH_AI_SOLUTION_CHANGED.getNewAiSolution().orElseThrow())
        .hasBranchId(BRANCH_WITH_AI_SOLUTION_CHANGED.getBranchIds().get(0));
  }

  @Test
  void branchWithAiSolutionChanged_successAndGrpcRequestWithBothChanges() {
    Try<Void> actual = underTest.save(BRANCH_WITH_ALL_CHANGES);

    assertSuccess(actual);

    verify(governanceBlockingStub).changeBranches(requestCaptor.capture());
    ChangeBranchesRequest actualRequest = requestCaptor.getValue();
    assertThatChangeBranchRequest(actualRequest)
        .hasSolutionChange(BRANCH_WITH_ALL_CHANGES.getNewAiSolution().orElseThrow())
        .hasStatusChange(BRANCH_WITH_ALL_CHANGES.getNewStatus().orElseThrow())
        .hasBranchId(BRANCH_WITH_ALL_CHANGES.getBranchIds().get(0));
  }

  static class ReasoningBranchUpdateRepositoryUpdateFixtures {

    static final TestUpdatedBranches NON_EXISTING_BRANCH =
        TestUpdatedBranches.builder().branchIds(List.of(BranchId.of(2, 2))).build();

    static final TestUpdatedBranches BRANCH_WITH_STATUS_CHANGED =
        TestUpdatedBranches
            .builder()
            .branchIds(List.of(BranchId.of(1, 4)))
            .newStatus(false)
            .build();

    static final TestUpdatedBranches BRANCH_WITH_AI_SOLUTION_CHANGED =
        TestUpdatedBranches
            .builder()
            .branchIds(List.of(BranchId.of(1, 4)))
            .newAiSolution("FALSE_POSITIVE")
            .build();

    static final TestUpdatedBranches BRANCH_WITH_UNKNOWN_SOLUTION_CHANGED =
        TestUpdatedBranches
            .builder()
            .branchIds(List.of(BranchId.of(1, 4)))
            .newAiSolution("UNKNOWN_AI_SOLUTION")
            .build();

    static final TestUpdatedBranches BRANCH_WITH_ALL_CHANGES =
        TestUpdatedBranches
            .builder()
            .branchIds(List.of(BranchId.of(1, 4)))
            .newStatus(true)
            .newAiSolution("HINTED_FALSE_POSITIVE")
            .build();

    static final TestUpdatedBranches TWO_BRANCHES_WITH_ALL_CHANGES =
        TestUpdatedBranches
            .builder()
            .branchIds(List.of(BranchId.of(20, 5), BranchId.of(20, 6)))
            .newAiSolution("HINTED_FALSE_POSITIVE")
            .newStatus(true)
            .build();

    static final StatusRuntimeException NOT_FOUND_STATUS_EXCEPTION = new StatusRuntimeException(
        Status.NOT_FOUND);
  }
}
