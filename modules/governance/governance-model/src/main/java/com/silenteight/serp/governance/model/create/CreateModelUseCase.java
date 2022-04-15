package com.silenteight.serp.governance.model.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.domain.ModelService;

import java.util.UUID;

@RequiredArgsConstructor
class CreateModelUseCase {

  @NonNull
  private final ModelService modelService;

  UUID activate(@NonNull CreateModelCommand command) {
    return modelService.createModel(
        command.getId(), command.getPolicy(), command.getCreatedBy());
  }
}
