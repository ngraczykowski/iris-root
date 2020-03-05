package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDetailsDto;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;
import com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchDetailsQueryFixtures.ReasoningBranch;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.silenteight.sens.webapp.grpc.reasoningbranch.BranchDetailsDtoAssert.assertThatDetails;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchDetailsQueryFixtures.DISABLED_REASONING_BRANCH;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchDetailsQueryFixtures.ENABLED_REASONING_BRANCH;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchDetailsQueryFixtures.NOT_FOUND_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchDetailsQueryFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GrpcReasoningBranchDetailsQueryTest {

  @Mock
  private BranchGovernanceBlockingStub stub;

  private GrpcReasoningBranchDetailsQuery underTest;
  private BranchSolutionMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new BranchSolutionMapper();
    underTest = new GrpcReasoningBranchDetailsQuery(mapper, stub);
  }

  @Test
  void returnsEmptyOptional_whenGrpcThrowsNotFoundStatusException() {
    given(stub.getReasoningBranch(any())).willThrow(NOT_FOUND_RUNTIME_EXCEPTION);

    Optional<BranchDetailsDto> actual = underTest.findByTreeIdAndBranchId(1, 2);

    assertThat(actual).isEmpty();
  }

  @Test
  void throwsGrpcCommunicationException_whenGrpcThrowsOtherThanNotFoundStatusException() {
    given(stub.getReasoningBranch(any())).willThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable when = () -> underTest.findByTreeIdAndBranchId(1, 2);

    assertThatThrownBy(when).isInstanceOf(GrpcCommunicationException.class);
  }

  @Test
  void returnsCorrectBranchDetails_whenRequestingEnabledBranch() {
    given(stub.getReasoningBranch(ENABLED_REASONING_BRANCH.request())).willReturn(
        ENABLED_REASONING_BRANCH.asResponse());

    Optional<BranchDetailsDto> actual =
        underTest.findByTreeIdAndBranchId(
            ENABLED_REASONING_BRANCH.getTreeId(),
            ENABLED_REASONING_BRANCH.getBranchId());

    assertThat(actual).isPresent();
    assertEquals(actual.get(), ENABLED_REASONING_BRANCH);
  }

  private void assertEquals(BranchDetailsDto actual, ReasoningBranch expected) {
    assertThatDetails(actual)
        .hasBranchId(expected.getBranchId())
        .hasAiSolution(mapper.map(expected.getSolution()))
        .hasEnabledSetTo(expected.isEnabled());
  }

  @Test
  void returnsCorrectBranchDetails_whenRequestingDisabledBranch() {
    given(stub.getReasoningBranch(DISABLED_REASONING_BRANCH.request())).willReturn(
        DISABLED_REASONING_BRANCH.asResponse());

    Optional<BranchDetailsDto> actual =
        underTest.findByTreeIdAndBranchId(
            DISABLED_REASONING_BRANCH.getTreeId(),
            DISABLED_REASONING_BRANCH.getBranchId());

    assertThat(actual).isPresent();
    assertEquals(actual.get(), DISABLED_REASONING_BRANCH);
  }
}
