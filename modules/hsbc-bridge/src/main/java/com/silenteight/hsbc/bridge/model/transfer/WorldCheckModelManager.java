package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;

import java.io.IOException;

import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.convertToModelStatusUpdated;
import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.createModelStatusUpdate;
import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.toModelPersisted;
import static com.silenteight.hsbc.bridge.model.transfer.ModelStatus.FAILURE;
import static com.silenteight.hsbc.bridge.model.transfer.ModelType.IS_PEP;
import static com.silenteight.hsbc.bridge.model.transfer.ModelType.NAME_ALIASES;

@Slf4j
@RequiredArgsConstructor
public class WorldCheckModelManager implements ModelManager {

  private final ModelClient jenkinsModelClient;
  private final StoreModelUseCase storeModelUseCase;
  private final WorldCheckMessageSender worldCheckMessageSender;
  private final ModelRepository modelRepository;

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
    var modelPersisted = toModelPersisted(request);
    worldCheckMessageSender.send(modelPersisted);
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
      return createModelStatusUpdate(request, modelUri);
    } catch (IOException e) {
      log.error("Unable to update model uri on minio: " + request.getName(), e);
      return convertToModelStatusUpdated(request, FAILURE);
    }
  }

  private boolean isPepOrNameAliases(ModelType type) {
    return type == IS_PEP || type == NAME_ALIASES;
  }
}
