package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.dto.ExportModelResponseDto;
import com.silenteight.hsbc.bridge.model.dto.FeatureDto;
import com.silenteight.hsbc.bridge.model.dto.ImportNewModelResponseDto;
import com.silenteight.hsbc.bridge.model.dto.SolvingModelDto;
import com.silenteight.model.api.v1.*;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import com.google.protobuf.ByteString;
import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.stream.Collectors;

import static com.google.protobuf.Empty.getDefaultInstance;
import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
class ModelGrpcAdapter implements ModelServiceClient {

  private final SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public SolvingModelDto getSolvingModel() {
    var solvingModel = getStub().getDefaultSolvingModel(getDefaultInstance());
    return mapSolvingModel(solvingModel);
  }

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public ExportModelResponseDto exportModel(String version) {
    var request = ExportModelRequest.newBuilder()
        .setVersion(version)
        .build();
    var exportModelResponse = getStub().exportModel(request);
    return mapToExportModelResponse(exportModelResponse);
  }

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public void sendStatus(String version) {
    var request = ModelDeployedOnProductionRequest.newBuilder()
        .setVersion(version)
        .build();
    getStub().modelDeployedOnProduction(request);
  }

  @Retryable(value = StatusRuntimeException.class)
  public ImportNewModelResponseDto importModel(byte[] model) {
    var request = ImportNewModelRequest.newBuilder()
        .setModelJson(ByteString.copyFrom(model))
        .build();
    var importNewModelResponse = getStub().importModel(request);
    return mapToImportNewModel(importNewModelResponse);
  }

  @Retryable(value = StatusRuntimeException.class)
  public void useModel(String model) {
    var request = UseModelRequest.newBuilder()
        .setModel(model)
        .build();
    getStub().useModel(request);
  }

  @Override
  public String transferModel(byte[] jsonModel) {
    var model = importModel(jsonModel).getModel();
    useModel(model);
    return model;
  }

  private SolvingModelServiceBlockingStub getStub() {
    return solvingModelServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }

  private SolvingModelDto mapSolvingModel(SolvingModel solvingModel) {
    return SolvingModelDto.builder()
        .name(solvingModel.getName())
        .policyName(solvingModel.getPolicyName())
        .strategyName(solvingModel.getStrategyName())
        .features(solvingModel.getFeaturesList().stream()
            .map(feature -> FeatureDto.builder()
                .name(feature.getName())
                .agentConfig(feature.getAgentConfig())
                .build())
            .collect(Collectors.toList()))
        .categories(solvingModel.getCategoriesList())
        .build();
  }

  private ExportModelResponseDto mapToExportModelResponse(ExportModelResponse response) {
    return ExportModelResponseDto.builder()
        .modelJson(response.getModelJson().toByteArray())
        .id(response.getId())
        .name(response.getName())
        .version(response.getVersion())
        .build();
  }

  private ImportNewModelResponseDto mapToImportNewModel(ImportNewModelResponse response) {
    return ImportNewModelResponseDto.builder().model(response.getModel()).build();
  }
}
