package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.ChangeBranchesRequest;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.grpc.reasoningbranch.ChangeBranchesRequestAssert.assertThatChangeBranchRequest;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchUpdateRepositoryTest.ReasoningBranchUpdateRepositoryUpdateFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class GrpcReasoningBranchUpdateRepositoryTest {

  @Mock
  private BranchGovernanceBlockingStub governanceBlockingStub;

  @InjectMocks
  private GrpcReasoningBranchUpdateRepository underTest;

  @Captor
  private ArgumentCaptor<ChangeBranchesRequest> requestCaptor;

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
        .hasStatusChange(BRANCH_WITH_STATUS_CHANGED.getNewIsActive().orElseThrow());
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
        .hasSolutionChange(BRANCH_WITH_AI_SOLUTION_CHANGED.getNewAiSolution().orElseThrow());
  }

  @Test
  void branchWithAiSolutionChanged_successAndGrpcRequestWithBothChanges() {
    Try<Void> actual = underTest.save(BRANCH_WITH_ALL_CHANGES);

    assertSuccess(actual);

    verify(governanceBlockingStub).changeBranches(requestCaptor.capture());
    ChangeBranchesRequest actualRequest = requestCaptor.getValue();
    assertThatChangeBranchRequest(actualRequest)
        .hasSolutionChange(BRANCH_WITH_ALL_CHANGES.getNewAiSolution().orElseThrow())
        .hasStatusChange(BRANCH_WITH_ALL_CHANGES.getNewIsActive().orElseThrow());
  }

  static class ReasoningBranchUpdateRepositoryUpdateFixtures {

    static final TestUpdatedBranch NON_EXISTING_BRANCH =
        TestUpdatedBranch.builder().treeId(2).branchId(2).build();

    static final TestUpdatedBranch BRANCH_WITH_STATUS_CHANGED =
        TestUpdatedBranch.builder().treeId(1).branchId(2).newStatus(false).build();

    static final TestUpdatedBranch BRANCH_WITH_AI_SOLUTION_CHANGED =
        TestUpdatedBranch
            .builder()
            .treeId(1)
            .branchId(3)
            .newAiSolution(BranchSolution.BRANCH_FALSE_POSITIVE.name())
            .build();

    static final TestUpdatedBranch BRANCH_WITH_UNKNOWN_SOLUTION_CHANGED =
        TestUpdatedBranch.builder().treeId(1).branchId(4).newAiSolution("BFP").build();

    static final TestUpdatedBranch BRANCH_WITH_ALL_CHANGES =
        TestUpdatedBranch
            .builder()
            .treeId(1)
            .branchId(4)
            .newStatus(true)
            .newAiSolution(BranchSolution.BRANCH_HINTED_FALSE_POSITIVE.name())
            .build();

    static final StatusRuntimeException NOT_FOUND_STATUS_EXCEPTION = new StatusRuntimeException(
        Status.NOT_FOUND);
  }
}
