package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc;
import com.silenteight.proto.serp.v1.api.DecisionTreeResponse;
import com.silenteight.proto.serp.v1.api.GetDecisionTreeRequest;
import com.silenteight.proto.serp.v1.api.ListDecisionTreesResponse;
import com.silenteight.serp.governance.app.grpc.GetDecisionTreeUseCase.GetDecisionTreeUseCaseListener;

import com.google.protobuf.Empty;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class DecisionTreeGrpcService extends DecisionTreeGovernanceGrpc.DecisionTreeGovernanceImplBase {

  private final GetDecisionTreeUseCase getUseCase;
  private final ListDecisionTreesUseCase listUseCase;

  @Override
  public void listDecisionTrees(
      Empty request,
      StreamObserver<ListDecisionTreesResponse> responseObserver) {

    responseObserver.onNext(listUseCase.activate());
    responseObserver.onCompleted();
  }

  @Override
  public void getDecisionTree(
      GetDecisionTreeRequest request,
      StreamObserver<DecisionTreeResponse> responseObserver) {

    getUseCase.activate(request, new GrpcGetDecisionTreeUseCaseListener(responseObserver));
  }

  @RequiredArgsConstructor
  private static class GrpcGetDecisionTreeUseCaseListener
      implements GetDecisionTreeUseCaseListener {

    private final StreamObserver<DecisionTreeResponse> responseObserver;

    @Override
    public void onDecisionTreeFound(DecisionTreeResponse response) {
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }

    @Override
    public void onDecisionTreeNotFound() {
      Status status = Status
          .newBuilder()
          .setCode(Code.NOT_FOUND_VALUE)
          .setMessage("Decision tree not found.")
          .build();

      responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }

    @Override
    public void onMissingDecisionTreeSpec() {
      Status status = Status
          .newBuilder()
          .setCode(Code.INVALID_ARGUMENT_VALUE)
          .setMessage("Missing decision tree spec definition (decision_group or decision_tree_id")
          .build();

      responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
  }
}
