package com.silenteight.hsbc.bridge.model.transfer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.hsbc.bridge.model.dto.ModelStatus;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.worldcheck.api.v1.ModelPersisted;

import java.net.URI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ModelMapper {

  private static final String SUCCESS = ModelStatus.SUCCESS.name();

  static ModelPersisted toModelPersisted(ModelInfoStatusRequest request) {
    return ModelPersisted.newBuilder()
        .setModelName(request.getName())
        .setModelUri(request.getUrl())
        .setModelType(com.silenteight.worldcheck.api.v1.ModelType.valueOf(request.getType().name()))
        .build();
  }

  static ModelStatusUpdatedDto createModelStatusUpdate(
      ModelInfoRequest modelInfoRequest, URI modelUri) {
    return ModelStatusUpdatedDto.builder()
        .name(modelInfoRequest.getName())
        .url(modelUri.toString())
        .type(modelInfoRequest.getType().name())
        .status(SUCCESS)
        .build();
  }

  static ModelStatusUpdatedDto convertToModelStatusUpdated(
      ModelInfoRequest modelInfoRequest, ModelStatus status) {
    return ModelStatusUpdatedDto.builder()
        .name(modelInfoRequest.getName())
        .url(modelInfoRequest.getUrl())
        .type(modelInfoRequest.getType().name())
        .status(status.name())
        .build();
  }

  static ModelStatusUpdatedDto convertToModelStatusUpdated(
      String model, ModelInfoRequest modelInfoRequest, ModelStatus status) {
    var updateUrl = updateUrl(modelInfoRequest.getUrl(), model);
    return ModelStatusUpdatedDto.builder()
        .name(model)
        .url(updateUrl)
        .type(modelInfoRequest.getType().name())
        .status(status.name())
        .build();
  }

  private static String updateUrl(String requestUri, String model) {
    var index = requestUri.lastIndexOf('/');
    var substring = requestUri.substring(0, index);
    return substring + "/" + model;
  }
}
