package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDto;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcFixtures.NOT_FOUND_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchesQueryTestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GrpcReasoningBranchesQueryTest {

  @Mock
  private BranchGovernanceBlockingStub stub;

  private GrpcReasoningBranchesQuery underTest;
  private BranchSolutionMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new BranchSolutionMapper();
    underTest = new GrpcReasoningBranchesQuery(mapper, stub);
  }

  @Test
  void returnsEmptyList_whenGrpcThrowsNotFoundStatusException() {
    given(stub.listReasoningBranches(any())).willThrow(NOT_FOUND_RUNTIME_EXCEPTION);

    List<BranchDto> actual =
        underTest.findByTreeIdAndBranchIds(DECISION_TREE_ID, REASONING_BRANCH_IDS);

    assertThat(actual).isEmpty();
  }

  @Test
  void throwsGrpcCommunicationException_whenGrpcThrowsOtherThanNotFoundStatusException() {
    given(stub.listReasoningBranches(any())).willThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable when = () ->
        underTest.findByTreeIdAndBranchIds(DECISION_TREE_ID, REASONING_BRANCH_IDS);

    assertThatThrownBy(when).isInstanceOf(GrpcCommunicationException.class);
  }

  @Test
  void returnsCorrectBranchDetails_whenRequestingEnabledBranch() {
    given(stub.listReasoningBranches(LIST_REASONING_BRANCHES_REQUEST)).willReturn(
        LIST_REASONING_BRANCHES_RESPONSE);

    List<BranchDto> actual =
        underTest.findByTreeIdAndBranchIds(DECISION_TREE_ID, REASONING_BRANCH_IDS);

    assertThat(actual).hasSize(1);
    assertThat(actual)
        .extracting(BranchDto::getReasoningBranchId)
        .containsOnly(EXISTING_REASONING_BRANCH_ID);
  }
}
