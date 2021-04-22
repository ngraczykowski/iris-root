package com.silenteight.serp.governance.model.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelNotFoundException;
import com.silenteight.serp.governance.model.get.GetModelDetailsQuery;

import java.util.UUID;

@RequiredArgsConstructor
class ModelQuery implements GetModelDetailsQuery {

  @NonNull
  private final ModelRepository modelRepository;

  public ModelDto get(@NonNull UUID modelId) {
    return modelRepository
        .findByModelId(modelId)
        .map(Model::toDto)
        .orElseThrow(() -> new ModelNotFoundException(modelId));
  }
}
