package com.silenteight.serp.governance.branchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.BranchSolutionGrpc;
import com.silenteight.proto.serp.v1.governance.ListAvailableBranchSolutionsResponse;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class BranchSolutionGrpcService extends BranchSolutionGrpc.BranchSolutionImplBase {

  private final BranchSolutionUseCase useCase;

  @Override
  public void listAvailableBranchSolutions(
      Empty request, StreamObserver<ListAvailableBranchSolutionsResponse> response) {

    response.onNext(useCase.listAvailableBranchSolutions());
    response.onCompleted();
  }
}
