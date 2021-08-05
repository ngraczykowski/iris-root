package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ExportModelResponseDto;
import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.dto.ModelType;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.hsbc.bridge.model.rest.output.ExportModelResponse;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.silenteight.hsbc.bridge.model.dto.ModelStatus.FAILURE;
import static com.silenteight.hsbc.bridge.model.dto.ModelType.IS_PEP_HISTORICAL;
import static com.silenteight.hsbc.bridge.model.dto.ModelType.IS_PEP_PROCEDURAL;
import static com.silenteight.hsbc.bridge.model.dto.ModelType.NAME_ALIASES;
import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.convertToModelStatusUpdated;
import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.createModelStatusUpdate;
import static com.silenteight.hsbc.bridge.model.transfer.ModelMapper.toModelPersisted;

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
    var modelPersisted = toModelPersisted(request);
    worldCheckMessageSender.send(modelPersisted);
  }

  @Override
  public ExportModelResponse exportModel(Details details) {
    var model = getModelUseCase.getModel(ModelType.valueOf(details.getType()));
    var modelJson = tryLoadModel(model.getMinIoUrl());
    var exportModelResponseDto = ExportModelResponseDto.builder()
        .modelJson(modelJson).build();
    return ExportModelResponseCreator.of(exportModelResponseDto).create();
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
    return type == IS_PEP_PROCEDURAL || type == IS_PEP_HISTORICAL || type == NAME_ALIASES;
  }

  private static class ModelLoadingException extends RuntimeException {

    private static final long serialVersionUID = 330005480374857470L;

    public ModelLoadingException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
