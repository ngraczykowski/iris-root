package com.silenteight.serp.governance.model.provide.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.ModelRequest;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc;
import com.silenteight.serp.governance.model.NonResolvableResourceException;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;

import com.google.protobuf.Empty;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;

import static com.google.rpc.Code.FAILED_PRECONDITION_VALUE;

@RequiredArgsConstructor
@Slf4j
class SolvingModelGrpcService
    extends SolvingModelServiceGrpc.SolvingModelServiceImplBase {

  private static final String MODEL_NOT_CONFIGURED_ERROR =
      "Requested model is not fully configured.";
  private static final String GET_DEFAULT_SOLVING_MODEL_ERROR =
      "Unhandled error occurred in Governance while calling 'getDefaultSolvingModel'.";
  private static final String MODEL_CANNOT_BE_RESOLVED_ERROR =
      "Some elements of the current configuration cannot be resolved. "
          + "Make sure all services are running";

  @NonNull
  private final DefaultModelQuery defaultModelQuery;

  @NonNull
  private final SolvingModelDetailsQuery solvingModelDetailsQuery;

  @NonNull
  private final SolvingModelProvider solvingModelProvider;

  @Override
  public void getDefaultSolvingModel(Empty request, StreamObserver<SolvingModel> responseObserver) {
    setSolvingModelOnResponseObserver(responseObserver, defaultModelQuery.getDefault());
  }

  @Override
  public void getSolvingModel(ModelRequest request, StreamObserver<SolvingModel> responseObserver) {
    setSolvingModelOnResponseObserver(
        responseObserver, solvingModelDetailsQuery.get(request.getModel()));
  }

  private void setSolvingModelOnResponseObserver(
      StreamObserver<SolvingModel> responseObserver, ModelDto modelDto) {

    try {
      SolvingModel solvingModel = solvingModelProvider.get(modelDto);
      responseObserver.onNext(solvingModel);
      responseObserver.onCompleted();
    } catch (NonResolvableResourceException e) {
      handleException(responseObserver, e, MODEL_CANNOT_BE_RESOLVED_ERROR);
    } catch (ModelMisconfiguredException e) {
      handleException(responseObserver, e, MODEL_NOT_CONFIGURED_ERROR);
    } catch (RuntimeException e) {
      handleException(responseObserver, e, GET_DEFAULT_SOLVING_MODEL_ERROR);
    }
  }

  private void handleException(
      StreamObserver<SolvingModel> responseObserver, RuntimeException e, String message) {

    Status status = Status
        .newBuilder()
        .setCode(FAILED_PRECONDITION_VALUE)
        .setMessage(message)
        .build();

    log.error(message, e);
    responseObserver.onError(StatusProto.toStatusRuntimeException(status));
  }
}
