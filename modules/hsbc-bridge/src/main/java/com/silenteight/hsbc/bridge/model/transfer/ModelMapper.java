package com.silenteight.hsbc.bridge.model.transfer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.hsbc.bridge.file.ResourceIdentifier;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.worldcheck.api.v1.ModelPersisted;

import static com.silenteight.hsbc.bridge.model.transfer.ModelStatus.SUCCESS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ModelMapper {

  static ModelPersisted toModelPersisted(ModelInfoStatusRequest request) {
    return ModelPersisted.newBuilder()
        .setModelName(request.getName())
        .setModelUri(request.getUrl())
        .setModelType(com.silenteight.worldcheck.api.v1.ModelType.valueOf(request.getType().name()))
        .build();
  }

  static ModelStatusUpdatedDto createModelStatusUpdate(
      ModelInfoRequest modelInfoRequest, ResourceIdentifier resource) {
    return ModelStatusUpdatedDto.builder()
        .name(modelInfoRequest.getName())
        .url(resource.getUri())
        .type(modelInfoRequest.getType().name())
        .status(SUCCESS.name())
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
}
