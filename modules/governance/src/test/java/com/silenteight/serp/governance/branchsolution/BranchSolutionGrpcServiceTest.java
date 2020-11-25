package com.silenteight.serp.governance.branchsolution;

import com.silenteight.proto.serp.v1.governance.BranchSolutionGrpc.BranchSolutionBlockingStub;
import com.silenteight.proto.serp.v1.governance.ListAvailableBranchSolutionsResponse;
import com.silenteight.sep.base.testing.grpc.GrpcServerExtension;

import com.google.protobuf.Empty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.proto.serp.v1.governance.BranchSolutionGrpc.newBlockingStub;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchSolutionGrpcServiceTest {

  @RegisterExtension
  GrpcServerExtension grpcServer = new GrpcServerExtension().directExecutor();

  @Mock
  private BranchSolutionUseCase useCase;

  private BranchSolutionBlockingStub branchStub;

  @BeforeEach
  void setUp() {
    grpcServer.addService(new BranchSolutionGrpcService(useCase));
    branchStub = newBlockingStub(grpcServer.getChannel());
  }

  @Test
  void returnsSolutionsReturnedByUseCase() {
    ListAvailableBranchSolutionsResponse response =
        ListAvailableBranchSolutionsResponse
            .newBuilder()
            .addBranchSolutions(BRANCH_FALSE_POSITIVE)
            .build();

    when(useCase.listAvailableBranchSolutions()).thenReturn(response);

    ListAvailableBranchSolutionsResponse actualResponse =
        branchStub.listAvailableBranchSolutions(Empty.newBuilder().build());

    assertThat(actualResponse).isEqualTo(response);
  }
}
