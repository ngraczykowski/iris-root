package com.silenteight.serp.governance.model.defaultmodel.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc;

import com.google.protobuf.Empty;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

import static com.google.rpc.Code.FAILED_PRECONDITION_VALUE;
import static com.google.rpc.Code.INTERNAL_VALUE;

@RequiredArgsConstructor
@Slf4j
class ModelGrpcService
    extends SolvingModelServiceGrpc.SolvingModelServiceImplBase {

  private static final String MODEL_NOT_CONFIGURED_ERROR =
      "Requested model is not fully configured.";
  private static final String GET_DEFAULT_SOLVING_MODEL_ERROR =
      "Unhandled error occurred in Governance while calling 'getDefaultSolvingModel'.";

  @NonNull
  private final DefaultModelQuery defaultModelQuery;

  @Override
  public void getDefaultSolvingModel(
      Empty request, StreamObserver<SolvingModel> responseObserver) {
    try {
      responseObserver.onNext(defaultModelQuery.get());
      responseObserver.onCompleted();
    } catch (ModelMisconfiguredException e) {
      Status status = Status.newBuilder()
          .setCode(FAILED_PRECONDITION_VALUE)
          .setMessage(MODEL_NOT_CONFIGURED_ERROR)
          .build();

      log.error(MODEL_NOT_CONFIGURED_ERROR, e);
      responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    } catch (RuntimeException e) {
      Status status = Status.newBuilder()
          .setCode(INTERNAL_VALUE)
          .setMessage(GET_DEFAULT_SOLVING_MODEL_ERROR)
          .build();

      log.error(GET_DEFAULT_SOLVING_MODEL_ERROR, e);
      responseObserver.onError(StatusProto.toStatusRuntimeException(status));
    }
  }

}
