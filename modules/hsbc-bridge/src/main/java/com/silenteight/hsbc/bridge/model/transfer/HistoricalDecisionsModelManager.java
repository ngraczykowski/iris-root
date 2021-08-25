package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.dto.ModelType;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.silenteight.hsbc.bridge.model.dto.ModelStatus.FAILURE;
import static com.silenteight.hsbc.bridge.model.dto.ModelType.HISTORICAL_DECISIONS_ALERTED_PARTY;
import static com.silenteight.hsbc.bridge.model.dto.ModelType.HISTORICAL_DECISIONS_MATCH;
import static com.silenteight.hsbc.bridge.model.dto.ModelType.HISTORICAL_DECISIONS_WATCHLIST_PARTY;
import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.convertToModelStatusUpdated;
import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.createModelStatusUpdate;
import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.toHistoricalDecisionsModelPersisted;

@Slf4j
@RequiredArgsConstructor
public class HistoricalDecisionsModelManager implements ModelManager {

  private final ModelClient jenkinsModelClient;
  private final ModelRepository modelRepository;
  private final GetModelUseCase getModelUseCase;
  private final StoreModelUseCase storeModelUseCase;
  private final ModelTransferModelLoader modelTransferModelLoader;
  private final HistoricalDecisionsMessageSender historicalDecisionsMessageSender;

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
    var modelPersisted = toHistoricalDecisionsModelPersisted(request);
    historicalDecisionsMessageSender.send(modelPersisted);
  }

  @Override
  public byte[] exportModel(Details modelDetails) {
    var model = getModelUseCase.getModel(ModelType.valueOf(modelDetails.getType()));
    return tryLoadModel(model.getMinIoUrl());
  }

  @Override
  public boolean supportsModelType(ModelType type) {
    return type == HISTORICAL_DECISIONS_ALERTED_PARTY
        || type == HISTORICAL_DECISIONS_WATCHLIST_PARTY
        || type == HISTORICAL_DECISIONS_MATCH;
  }

  public void transferHistoricalDecisionsModelStatus(ModelStatusUpdatedDto modelStatusUpdated) {
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

  private byte[] tryLoadModel(String minioUrl) {
    try {
      var uri = new URI(minioUrl);
      return IOUtils.toByteArray(modelTransferModelLoader.loadModel(uri));
    } catch (IOException | URISyntaxException e) {
      log.error("Unable to load model from minio uri: " + minioUrl, e);
      throw new ModelLoadingException("Unable to load model from minio uri: " + e.getMessage(), e);
    }
  }
}
