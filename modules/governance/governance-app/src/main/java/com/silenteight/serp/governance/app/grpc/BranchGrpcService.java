package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.api.*;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class BranchGrpcService extends BranchGovernanceGrpc.BranchGovernanceImplBase {

  private final ListReasoningBranchesUseCase listReasoningBranchesUseCase;
  private final GetReasoningBranchUseCase getReasoningBranchUseCase;
  private final GetVectorSolutionsUseCase getVectorSolutionsUseCase;
  private final GetReasoningBranchIdCollectionUseCase getReasoningBranchIdCollectionUseCase;

  @Override
  public void listReasoningBranches(
      ListReasoningBranchesRequest request,
      StreamObserver<ListReasoningBranchesResponse> response) {

    response.onNext(listReasoningBranchesUseCase.activate(request));

    response.onCompleted();
  }

  @Override
  public void getReasoningBranch(
      GetReasoningBranchRequest request,
      StreamObserver<ReasoningBranchResponse> response) {

    response.onNext(getReasoningBranchUseCase.activate(request));

    response.onCompleted();
  }

  @Override
  public void getVectorSolutions(
      GetVectorSolutionsRequest request,
      StreamObserver<VectorSolutionsResponse> response) {

    response.onNext(getVectorSolutionsUseCase.activate(request));
    response.onCompleted();
  }

  @Override
  public void getReasoningBranchIdCollection(
      GetReasoningBranchIdCollectionRequest request,
      StreamObserver<GetReasoningBranchIdCollectionResponse> responseObserver) {

    responseObserver.onNext(getReasoningBranchIdCollectionUseCase.activate(request));
    responseObserver.onCompleted();
  }
}
