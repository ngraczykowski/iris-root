package com.silenteight.hsbc.bridge.model.transfer;

import lombok.Value;

import com.silenteight.hsbc.bridge.model.dto.ChangeType;
import com.silenteight.hsbc.bridge.model.dto.ModelInfo;

@Value(staticConstructor = "of")
class ModelInfoCreator {

  ModelInformationEntity modelInformationEntity;

  private static final String MINOR = ChangeType.MINOR.name();

  ModelInfo create(String address) {
    var name = modelInformationEntity.getName();
    var type = modelInformationEntity.getType().name();

    return ModelInfo.builder()
        .name(name)
        .url(address + "/model/export/" + type + name)
        .type(type)
        .changeType(MINOR)
        .build();
  }
}
