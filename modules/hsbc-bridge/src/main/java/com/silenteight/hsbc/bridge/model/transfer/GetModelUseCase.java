package com.silenteight.hsbc.bridge.model.transfer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelType;

import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.hsbc.bridge.model.dto.ModelStatus.SUCCESS;

@RequiredArgsConstructor
@Slf4j
class GetModelUseCase {

  private final ModelInformationRepository modelInformationRepository;

  @Transactional
  public ModelInformationEntity getModel(@NonNull ModelType type) {
    var modelEntity =
        modelInformationRepository.findFirstByTypeAndStatusOrderByCreatedAtDesc(type, SUCCESS);

    if (modelEntity.isPresent()) {
      var model = modelEntity.get();
      log.debug("Model for export: {} has been taken", model);
      return model;
    } else {
      log.error("Model with given type: " + type + " doesn't exist in database");
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
