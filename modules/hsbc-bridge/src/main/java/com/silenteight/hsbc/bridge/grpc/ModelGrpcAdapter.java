package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.dto.ExportModelResponseDto;
import com.silenteight.hsbc.bridge.model.dto.FeatureDto;
import com.silenteight.hsbc.bridge.model.dto.ImportNewModelResponseDto;
import com.silenteight.hsbc.bridge.model.dto.SolvingModelDto;
import com.silenteight.model.api.v1.*;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class ModelGrpcAdapter implements ModelServiceClient {

  private final SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public SolvingModelDto getSolvingModel() {
    var solvingModel = getStub().getDefaultSolvingModel(Empty.getDefaultInstance());
    return mapSolvingModel(solvingModel);
  }

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public ExportModelResponseDto exportModel(String version) {
    var request = ExportModelRequest.newBuilder()
        .setVersion(version)
        .build();
    var exportModelResponse = getStub().exportModel(request);
    var exportModelResponseDto = mapToExportModelResponse(exportModelResponse);
    log.info(
        "Response model from Governance after exportModel grpc call: {}",
        new String(exportModelResponseDto.getModelJson(), StandardCharsets.UTF_8));
    return exportModelResponseDto;
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
    log.info("Imported model from Nexus as request to grpc call importModel, model length: "
        + model.length);
    var request = ImportNewModelRequest.newBuilder()
        .setModelJson(ByteString.copyFrom(model))
        .build();
    var importNewModelResponse = getStub().importModel(request);
    var importNewModelResponseDto = mapToImportNewModel(importNewModelResponse);
    log.info(
        "Response model from Governance after importModel grpc call : {}",
        importNewModelResponseDto.getModel());
    return importNewModelResponseDto;
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
    return solvingModelServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
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
