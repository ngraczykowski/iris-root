package com.silenteight.serp.governance.model.provide.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.*;
import com.silenteight.serp.governance.model.NonResolvableResourceException;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.model.transfer.importing.ImportModelUseCase;

import com.google.protobuf.Empty;
import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

import static com.google.rpc.Code.FAILED_PRECONDITION_VALUE;
import static com.google.rpc.Code.INTERNAL_VALUE;
import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;
import static com.silenteight.serp.governance.model.common.ModelResource.toResourceName;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

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
  @NonNull
  private final ImportModelUseCase importModelUseCase;

  @Override
  public void getDefaultSolvingModel(Empty request, StreamObserver<SolvingModel> responseObserver) {
    setSolvingModelOnResponseObserver(responseObserver, defaultModelQuery.getDefault());
  }

  @Override
  public void getSolvingModel(ModelRequest request, StreamObserver<SolvingModel> responseObserver) {
    setSolvingModelOnResponseObserver(
        responseObserver, solvingModelDetailsQuery.get(request.getModel()));
  }

  @Override
  public void importModel(
      ImportNewModelRequest request, StreamObserver<ImportNewModelResponse> responseObserver) {

    try {
      UUID modelId = importModelUseCase.apply(toBase64String(request.getModelJson()));
      ImportNewModelResponse response = ImportNewModelResponse.newBuilder()
          .setModel(toResourceName(modelId))
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (NonResolvableResourceException e) {
      handleException(
          responseObserver, e, FAILED_PRECONDITION_VALUE, MODEL_CANNOT_BE_RESOLVED_ERROR);
    } catch (ModelMisconfiguredException e) {
      handleException(responseObserver, e, FAILED_PRECONDITION_VALUE, MODEL_NOT_CONFIGURED_ERROR);
    } catch (RuntimeException e) {
      handleException(responseObserver, e, INTERNAL_VALUE, GET_DEFAULT_SOLVING_MODEL_ERROR);
    }
  }

  private void setSolvingModelOnResponseObserver(
      StreamObserver<SolvingModel> responseObserver, ModelDto modelDto) {

    try {
      SolvingModel solvingModel = solvingModelProvider.get(modelDto);
      responseObserver.onNext(solvingModel);
      responseObserver.onCompleted();
    } catch (NonResolvableResourceException e) {
      handleException(
          responseObserver, e, FAILED_PRECONDITION_VALUE, MODEL_CANNOT_BE_RESOLVED_ERROR);
    } catch (ModelMisconfiguredException e) {
      handleException(responseObserver, e, FAILED_PRECONDITION_VALUE, MODEL_NOT_CONFIGURED_ERROR);
    } catch (RuntimeException e) {
      handleException(responseObserver, e, INTERNAL_VALUE, GET_DEFAULT_SOLVING_MODEL_ERROR);
    }
  }

  private <T> void handleException(
      StreamObserver<T> responseObserver, RuntimeException e, Integer code, String message) {

    Status status = Status.newBuilder()
        .setCode(code)
        .setMessage(message)
        .build();

    log.error(message, e);
    responseObserver.onError(toStatusRuntimeException(status));
  }
}
