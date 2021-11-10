package com.silenteight.governance.api.library.v1.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.ExportModelRequest;
import com.silenteight.model.api.v1.ImportNewModelRequest;
import com.silenteight.model.api.v1.ModelDeployedOnProductionRequest;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;
import com.silenteight.model.api.v1.UseModelRequest;

import com.google.protobuf.ByteString;
import io.vavr.control.Try;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.google.protobuf.Empty.getDefaultInstance;

@Slf4j
@RequiredArgsConstructor
public class ModelGrpcAdapter implements ModelServiceClient {

  public static final String COULD_NOT_GET_SOLVING_MODEL_ERROR_MSG = "Could not get solving model";
  public static final String COULD_NOT_EXPORT_MODEL_ERROR_MSG = "Could not export model";
  public static final String COULD_NOT_IMPORT_MODEL_ERROR_MSG = "Could not import model";
  public static final String COULD_NOT_USE_MODEL_ERROR_MSG = "Could not use model";
  public static final String COULD_NOT_UPDATE_STATUS_ERROR_MSG = "Could not update status";

  private final SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public SolvingModelOut getSolvingModel() {
    return Try
        .of(() -> getStub().getDefaultSolvingModel(getDefaultInstance()))
        .map(SolvingModelOut::createFrom)
        .onFailure(e -> log.error(COULD_NOT_GET_SOLVING_MODEL_ERROR_MSG, e))
        .getOrElseThrow(
            e -> new GovernanceLibraryRuntimeException(COULD_NOT_GET_SOLVING_MODEL_ERROR_MSG, e));
  }

  @Override
  public ExportModelOut exportModel(String version) {
    return Try
        .of(() -> getStub().exportModel(getExportModelRequest(version)))
        .map(ExportModelOut::createFrom)
        .onSuccess(
            response -> log.info(
                "Response model from Governance after exportModel grpc call: {}",
                new String(response.getModelJson(), StandardCharsets.UTF_8)))
        .onFailure(e -> log.error(COULD_NOT_EXPORT_MODEL_ERROR_MSG, e))
        .getOrElseThrow(
            e -> new GovernanceLibraryRuntimeException(COULD_NOT_EXPORT_MODEL_ERROR_MSG, e));
  }

  @Override
  public void sendStatus(String version) {
    Try
        .run(
            () -> getStub().modelDeployedOnProduction(getModelDeployedOnProductionRequest(version)))
        .onFailure(e -> log.error(COULD_NOT_UPDATE_STATUS_ERROR_MSG, e))
        .getOrElseThrow(
            e -> new GovernanceLibraryRuntimeException(COULD_NOT_UPDATE_STATUS_ERROR_MSG, e));
  }

  @Override
  public String transferModel(byte[] jsonModel) {
    var model = importModel(jsonModel).getModel();
    useModel(model);
    return model;
  }

  private ImportNewModelOut importModel(byte[] model) {
    log.info(
        "Imported model from Nexus as request to grpc call importModel: {}",
        new String(model, StandardCharsets.UTF_8));

    return Try
        .of(() -> getStub().importModel(getImportNewModelRequest(model)))
        .map(ImportNewModelOut::createFrom)
        .onSuccess(
            response -> log.info(
                "Response model from Governance after importModel grpc call : {}",
                response.getModel()))
        .onFailure(e -> log.error(COULD_NOT_IMPORT_MODEL_ERROR_MSG, e))
        .getOrElseThrow(
            e -> new GovernanceLibraryRuntimeException(COULD_NOT_IMPORT_MODEL_ERROR_MSG, e));
  }

  public void useModel(String model) {
    Try
        .run(() -> getStub().useModel(getUseModelRequest(model)))
        .onFailure(e -> log.error(COULD_NOT_USE_MODEL_ERROR_MSG, e))
        .getOrElseThrow(
            e -> new GovernanceLibraryRuntimeException(COULD_NOT_USE_MODEL_ERROR_MSG, e));
  }

  private UseModelRequest getUseModelRequest(String model) {
    return UseModelRequest.newBuilder()
        .setModel(model)
        .build();
  }

  private ImportNewModelRequest getImportNewModelRequest(byte[] model) {
    return ImportNewModelRequest.newBuilder()
        .setModelJson(ByteString.copyFrom(model))
        .build();
  }

  private ModelDeployedOnProductionRequest getModelDeployedOnProductionRequest(String version) {
    return ModelDeployedOnProductionRequest.newBuilder()
        .setVersion(version)
        .build();
  }

  private ExportModelRequest getExportModelRequest(String version) {
    return ExportModelRequest.newBuilder()
        .setVersion(version)
        .build();
  }

  private SolvingModelServiceBlockingStub getStub() {
    return solvingModelServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
