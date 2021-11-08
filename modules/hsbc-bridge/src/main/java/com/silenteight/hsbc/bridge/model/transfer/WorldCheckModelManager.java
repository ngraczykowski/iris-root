package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelStatus;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.dto.ModelType;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
public class WorldCheckModelManager implements ModelManager {

  private final ModelClient jenkinsModelClient;
  private final StoreModelUseCase storeModelUseCase;
  private final GetModelUseCase getModelUseCase;
  private final WorldCheckMessageSender worldCheckMessageSender;
  private final ModelRepository modelRepository;
  private final ModelTransferModelLoader modelTransferModelLoader;

  @Override
  public void transferModelToJenkins(ModelInfo modelInfo) {
    jenkinsModelClient.updateModel(modelInfo);
  }

  @Override
  public void transferModelFromJenkins(ModelInfoRequest request) {
    var modelStatusUpdated = trySavingModel(request);
    jenkinsModelClient.sendModelStatus(modelStatusUpdated);
  }

  @Override
  public void transferModelStatus(ModelInfoStatusRequest request) {
    var modelPersisted = ModelMapper.toModelPersisted(request);
    worldCheckMessageSender.send(modelPersisted);
  }

  @Override
  public byte[] exportModel(Details details) {
    var model = getModelUseCase.getModel(ModelType.valueOf(details.getType()));
    return tryLoadModel(model.getMinIoUrl());
  }

  @Override
  public boolean supportsModelType(ModelType modelType) {
    return isPepOrNameAliases(modelType);
  }

  public void transferWorldCheckModelStatus(ModelStatusUpdatedDto modelStatusUpdated) {
    storeModelUseCase.storeModel(modelStatusUpdated);
  }

  private ModelStatusUpdatedDto trySavingModel(ModelInfoRequest request) {
    try {
      var modelUri = modelRepository.saveModel(request.getUrl(), request.getName());
      return ModelMapper.createModelStatusUpdate(request, modelUri);
    } catch (IOException e) {
      log.error("Unable to update model uri on minio: " + request.getName(), e);
      return ModelMapper.convertToModelStatusUpdated(request, ModelStatus.FAILURE);
    }
  }

  private byte[] tryLoadModel(String minioUrl) {
    try {
      var uri = new URI(minioUrl);
      return IOUtils.toByteArray(modelTransferModelLoader.loadModel(uri));
    } catch (IOException | URISyntaxException e) {
      log.error("Unable to load model from minio uri: " + minioUrl, e);
      throw new ModelLoadingException("Unable to load model from minio uri: " + e.getMessage(), e);
    }
  }

  private boolean isPepOrNameAliases(ModelType type) {
    return type == ModelType.IS_PEP_PROCEDURAL || type == ModelType.IS_PEP_HISTORICAL || type == ModelType.NAME_ALIASES;
  }
}
