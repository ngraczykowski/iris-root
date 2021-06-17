package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class StoreModelUseCase {

  private final ModelInformationRepository modelInformationRepository;

  @Transactional
  public void storeModel(ModelStatusUpdatedDto modelStatusUpdated) {
    var modelEntity = toModelEntity(modelStatusUpdated);
    modelInformationRepository.save(modelEntity);

    log.info("New model: {} has been stored", modelEntity);
  }

  private ModelInformationEntity toModelEntity(ModelStatusUpdatedDto modelStatusUpdated) {
    return ModelInformationEntity.builder()
        .name(modelStatusUpdated.getName())
        .minIoUrl(modelStatusUpdated.getUrl())
        .type(ModelType.valueOf(modelStatusUpdated.getType()))
        .build();
  }
}
