package com.silenteight.sens.webapp.grpc.configuration.solution;

import com.silenteight.proto.serp.v1.governance.BranchSolutionGrpc.BranchSolutionBlockingStub;
import com.silenteight.proto.serp.v1.governance.ListAvailableBranchSolutionsResponse;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import com.google.protobuf.Empty;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.*;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.configuration.solution.GrpcSolutionsQueryTest.GrpcSolutionsQueryFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GrpcSolutionsQueryTest {

  @InjectMocks
  private GrpcSolutionsQuery underTest;

  @Mock
  private BranchSolutionBlockingStub solutionStub;

  @Test
  void throwsGrpcCommunicationException_whenGrpcThrowsOtherThanNotFoundStatusException() {
    given(solutionStub.listAvailableBranchSolutions(Empty.getDefaultInstance()))
        .willThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable when = () -> underTest.list();

    assertThatThrownBy(when).isInstanceOf(GrpcCommunicationException.class);
  }

  @Test
  void returnsSolutions_whenRequestingSolutions() {
    given(solutionStub.listAvailableBranchSolutions(Empty.getDefaultInstance()))
        .willReturn(
            ListAvailableBranchSolutionsResponse
                .newBuilder()
                .addAllBranchSolutions(
                    List.of(
                        BRANCH_NO_DECISION,
                        BRANCH_FALSE_POSITIVE,
                        BRANCH_HINTED_FALSE_POSITIVE,
                        BRANCH_POTENTIAL_TRUE_POSITIVE,
                        BRANCH_HINTED_POTENTIAL_TRUE_POSITIVE))
                .build());

    List<String> actual = underTest.list();

    assertThat(actual).hasSize(5);
    assertThat(actual).containsExactly(
        NO_DECISION_SOLUTION,
        FALSE_POSITIVE_SOLUTION,
        HINTED_FALSE_POSITIVE_SOLUTION,
        POTENTIAL_TRUE_POSITIVE_SOLUTION,
        HINTED_POTENTIAL_TRUE_POSITIVE_SOLUTION);
  }

  static class GrpcSolutionsQueryFixtures {

    static final String NO_DECISION_SOLUTION = "NO_DECISION";
    static final String FALSE_POSITIVE_SOLUTION = "FALSE_POSITIVE";
    static final String HINTED_FALSE_POSITIVE_SOLUTION = "HINTED_FALSE_POSITIVE";
    static final String POTENTIAL_TRUE_POSITIVE_SOLUTION = "POTENTIAL_TRUE_POSITIVE";
    static final String HINTED_POTENTIAL_TRUE_POSITIVE_SOLUTION = "HINTED_POTENTIAL_TRUE_POSITIVE";
  }
}
