package com.silenteight.serp.governance.model.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.*;
import com.silenteight.serp.governance.model.NonResolvableResourceException;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.model.grpc.exception.InvalidExportModelRequestException;
import com.silenteight.serp.governance.model.grpc.exception.InvalidModelDeployedOnProductionRequestException;
import com.silenteight.serp.governance.model.provide.DefaultModelQuery;
import com.silenteight.serp.governance.model.provide.SolvingModelDetailsQuery;
import com.silenteight.serp.governance.model.provide.SolvingModelQuery;
import com.silenteight.serp.governance.model.transfer.dto.TransferredModelRootDto;
import com.silenteight.serp.governance.model.transfer.export.ExportModelUseCase;
import com.silenteight.serp.governance.model.transfer.importing.ImportModelUseCase;
import com.silenteight.serp.governance.model.use.UseModelUseCase;
import com.silenteight.serp.governance.model.used.MarkModelAsUsedOnProductionUseCase;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.UUID;
import javax.validation.constraints.NotNull;

import static com.google.rpc.Code.FAILED_PRECONDITION_VALUE;
import static com.google.rpc.Code.INTERNAL_VALUE;
import static com.silenteight.serp.governance.model.common.ModelResource.toResourceName;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@RequiredArgsConstructor
@Slf4j
class SolvingModelGrpcService extends SolvingModelServiceGrpc.SolvingModelServiceImplBase {

  private static final String MODEL_NOT_CONFIGURED_ERROR =
      "Requested model is not fully configured.";
  private static final String GET_DEFAULT_SOLVING_MODEL_ERROR =
      "Unhandled error occurred in Governance while calling 'getDefaultSolvingModel'.";
  private static final String MODEL_CANNOT_BE_RESOLVED_ERROR =
      "Some elements of the current configuration cannot be resolved. "
          + "Make sure all services are running";
  private static final String IMPORT_MODEL_ERROR =
      "Unhandled error occurred in Governance while calling 'importModel'.";
  private static final String USE_MODEL_ERROR =
      "Unhandled error occurred in Governance while calling 'useModel'.";

  @NonNull
  private final DefaultModelQuery defaultModelQuery;
  @NonNull
  private final SolvingModelDetailsQuery solvingModelDetailsQuery;
  @NonNull
  private final SolvingModelQuery solvingModelQuery;
  @NonNull
  private final ExportModelUseCase exportModelUseCase;
  @NonNull
  private final ImportModelUseCase importModelUseCase;
  @NonNull
  private final UseModelUseCase useModelUseCase;
  @NonNull
  private final MarkModelAsUsedOnProductionUseCase markModelUsedOnProductionUseCase;

  @Override
  public void getDefaultSolvingModel(Empty request, StreamObserver<SolvingModel> responseObserver) {
    log.info("Getting default solving model");
    setSolvingModelOnResponseObserver(responseObserver, defaultModelQuery.getDefault());
  }

  @Override
  public void getSolvingModel(ModelRequest request, StreamObserver<SolvingModel> responseObserver) {
    log.info("ModelRequest request received, request={}", request);
    setSolvingModelOnResponseObserver(
        responseObserver, solvingModelDetailsQuery.get(request.getModel()));
  }

  @Override
  public void importModel(
      ImportNewModelRequest request, StreamObserver<ImportNewModelResponse> responseObserver) {

    log.info("ImportNewModelRequest request received, request={}", request);
    try {
      UUID modelId = importModelUseCase.apply(request.getModelJson().toStringUtf8());
      ImportNewModelResponse response = ImportNewModelResponse.newBuilder()
          .setModel(toResourceName(modelId))
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      handleException(responseObserver, e, INTERNAL_VALUE, IMPORT_MODEL_ERROR);
    }
    log.debug("ImportNewModelRequest request processed.");
  }

  @Override
  public void useModel(
      UseModelRequest request, StreamObserver<Empty> responseObserver) {

    log.info("UseModelRequest request received, request={}", request);
    try {
      useModelUseCase.apply(request.getModel());
      responseObserver.onNext(Empty.newBuilder().build());
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      handleException(responseObserver, e, INTERNAL_VALUE, USE_MODEL_ERROR);
    }
    log.debug("UseModelRequest request processed.");
  }

  private void setSolvingModelOnResponseObserver(
      StreamObserver<SolvingModel> responseObserver, ModelDto modelDto) {

    try {
      SolvingModel solvingModel = solvingModelQuery.get(modelDto);
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

  @Override
  public void exportModel(
      ExportModelRequest request,
      StreamObserver<ExportModelResponse> responseObserver) {

    log.info("ExportModelRequest request received, request={}", request);

    try {
      TransferredModelRootDto modelToExport;
      switch (request.getModelCase()) {
        case NAME:
          modelToExport = exportModelUseCase.applyById(request.getName());
          break;
        case VERSION:
          modelToExport = exportModelUseCase.applyByVersion(request.getVersion());
          break;
        default:
          throw new InvalidExportModelRequestException();
      }
      responseObserver.onNext(toResponse(modelToExport));
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      handleException(responseObserver, e, INTERNAL_VALUE, USE_MODEL_ERROR);
    }
    log.debug("ExportModelRequest request processed.");
  }

  @NotNull
  private static ExportModelResponse toResponse(TransferredModelRootDto modelToExport) {
    return ExportModelResponse.newBuilder()
        .setModelJson(ByteString.copyFromUtf8(modelToExport.toJson()))
        .setVersion(modelToExport.getModelVersion())
        .setName(modelToExport.getModelName())
        .build();
  }

  @Override
  public void modelDeployedOnProduction(
      ModelDeployedOnProductionRequest request,
      StreamObserver<Empty> responseObserver) {

    log.info("ModelDeployedOnProductionRequest request received, request={}", request);
    try {
      switch (request.getModelCase()) {
        case NAME:
          markModelUsedOnProductionUseCase.applyByName(request.getName());
          break;
        case VERSION:
          markModelUsedOnProductionUseCase.applyByVersion(request.getVersion());
          break;
        default:
        case MODEL_NOT_SET:
          throw new InvalidModelDeployedOnProductionRequestException();
      }
      responseObserver.onNext(Empty.newBuilder().build());
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      handleException(responseObserver, e, INTERNAL_VALUE, USE_MODEL_ERROR);
    }
    log.debug("ModelDeployedOnProductionRequest request processed.");
  }
}
