package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.file.SaveResourceUseCase;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;

import java.io.IOException;
import java.net.URL;

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
  private final SaveResourceUseCase saveResourceUseCase;
  private final StoreModelUseCase storeModelUseCase;
  private final WorldCheckMessageSender worldCheckMessageSender;

  @Override
  public void transferModelToJenkins(ModelInfo modelInfo) {
    jenkinsModelClient.updateModel(modelInfo);
  }

  @Override
  public void transferModelFromJenkins(ModelInfoRequest request) {
    var modelStatusUpdated = transferModelToMinIo(request);
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

  private ModelStatusUpdatedDto transferModelToMinIo(ModelInfoRequest request) {
    try {
      var url = new URL(request.getUrl());
      var inputStream = url.openStream();
      var resource = saveResourceUseCase.save(inputStream, request.getName());
      log.info("Update model uri on minio successful!");
      return createModelStatusUpdate(request, resource);
    } catch (IOException e) {
      log.error("Unable to update model uri on minio: " + request.getName(), e);
      return convertToModelStatusUpdated(request, FAILURE);
    }
  }

  private boolean isPepOrNameAliases(ModelType type) {
    return type == IS_PEP || type == NAME_ALIASES;
  }
}
