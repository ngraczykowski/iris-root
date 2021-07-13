package com.silenteight.hsbc.bridge.model.transfer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class GetModelUseCase {

  private final ModelInformationRepository modelInformationRepository;

  @Transactional
  public ModelInformationEntity getModel(@NonNull String type) {
    var modelEntity =
        modelInformationRepository.findFirstByTypeOrderByCreatedAtDesc(ModelType.valueOf(type));

    if (modelEntity.isPresent()) {
      var model = modelEntity.get();
      log.debug("Model for export: {} has been taken", model);
      return model;
    } else {
      throw new ModelNotFoundException(
          "Model with given type: " + type + " doesn't exist in database");
    }
  }

  private static final class ModelNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5952313234534123321L;

    public ModelNotFoundException(String message) {
      super(message);
    }
  }
}
