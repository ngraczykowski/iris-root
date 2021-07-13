package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.hsbc.bridge.model.rest.output.ExportModelResponse;

import java.io.IOException;

import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.convertToModelStatusUpdated;
import static com.silenteight.hsbc.bridge.model.transfer.ModelStatus.FAILURE;
import static com.silenteight.hsbc.bridge.model.transfer.ModelStatus.SUCCESS;
import static com.silenteight.hsbc.bridge.model.transfer.ModelType.MODEL;

@Slf4j
@RequiredArgsConstructor
public class GovernanceModelManager implements ModelManager {

  private final ModelClient jenkinsModelClient;
  private final RepositoryClient nexusModelClient;
  private final ModelServiceClient governanceServiceClient;

  @Override
  public void transferModelToJenkins(ModelInfo modelInfo) {
    jenkinsModelClient.updateModel(modelInfo);
  }

  @Override
  public void transferModelFromJenkins(ModelInfoRequest request) {
    var modelStatusUpdated = transferModelFromNexus(request);
    jenkinsModelClient.sendModelStatus(modelStatusUpdated);
  }

  @Override
  public void transferModelStatus(ModelInfoStatusRequest request) {
    governanceServiceClient.sendStatus(request.getName());
  }

  @Override
  public ExportModelResponse exportModel(Details details) {
    var exportModelResponseDto = governanceServiceClient.exportModel(details.getName());
    return ExportModelResponseCreator.of(exportModelResponseDto).create();
  }

  @Override
  public boolean supportsModelType(ModelType modelType) {
    return modelType == MODEL;
  }

  private ModelStatusUpdatedDto transferModelFromNexus(ModelInfoRequest request) {
    try {
      var jsonModel = nexusModelClient.updateModel(request.getUrl());
      governanceServiceClient.transferModel(jsonModel);
      log.info("Update model successful!");
      return convertToModelStatusUpdated(request, SUCCESS);
    } catch (IOException e) {
      log.error("Unable to update model: " + request.getName(), e);
      return convertToModelStatusUpdated(request, FAILURE);
    }
  }
}
