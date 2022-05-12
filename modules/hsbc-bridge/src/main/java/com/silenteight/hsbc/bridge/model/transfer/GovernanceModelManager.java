package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelStatus;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.dto.ModelType;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;

import java.io.IOException;

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
  public ModelStatusUpdatedDto transferModelFromJenkins(ModelInfoRequest request) {
    return transferModelFromNexus(request);
  }

  @Override
  public void transferModelStatus(ModelInfoRequest request) {
    // No need to do anything - "use model" already sent to Governance
  }

  @Override
  public byte[] exportModel(Details details) {
    var exportModelResponseDto = governanceServiceClient.exportModel(details.getVersion());
    return exportModelResponseDto.getModelJson();
  }

  @Override
  public boolean supportsModelType(ModelType modelType) {
    return modelType == ModelType.MODEL;
  }

  private ModelStatusUpdatedDto transferModelFromNexus(ModelInfoRequest request) {
    try {
      var jsonModel = nexusModelClient.updateModel(request.getUrl());
      log.info("Transferring model length = {}", jsonModel.length);
      var model = governanceServiceClient.transferModel(jsonModel);
      log.info("Update model successful: {}", model);
      return ModelMapper.convertToModelStatusUpdated(request, ModelStatus.SUCCESS);
    } catch (IOException | RuntimeException e) {
      log.error("Unable to update model: " + request.getName(), e);
      return ModelMapper.convertToModelStatusUpdated(request, ModelStatus.FAILURE);
    }
  }
}
