package com.silenteight.serp.governance.model.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.model.domain.exception.ModelNotFoundException;
import com.silenteight.serp.governance.model.domain.exception.TooManyModelsException;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.model.provide.DefaultModelQuery;
import com.silenteight.serp.governance.model.provide.SolvingModelDetailsQuery;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.model.common.ModelResource.fromResourceName;
import static java.time.OffsetDateTime.now;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class ModelQuery
    implements ModelDetailsQuery, DefaultModelQuery, SolvingModelDetailsQuery {

  @NonNull
  private final ModelRepository modelRepository;

  @NonNull
  private final CurrentPolicyProvider currentPolicyProvider;

  private static final String DEFAULT_MODEL_NAME = "solvingModels/default";

  public ModelDto get(@NonNull UUID modelId) {
    return modelRepository
        .findByModelId(modelId)
        .map(Model::toDto)
        .orElseThrow(() -> new ModelNotFoundException(modelId));
  }

  @Override
  public List<ModelDto> getByPolicy(@NonNull String policy) {
    List<ModelDto> models = modelRepository
        .findAllByPolicyName(policy)
        .stream()
        .map(Model::toDto)
        .collect(toList());

    if (models.size() > 1)
      throw new TooManyModelsException(policy);

    return models;
  }

  @Override
  public UUID getModelIdByVersion(String version) {
    return modelRepository
        .findModelByModelVersion(version)
        .map(Model::getModelId)
        .orElseThrow(() -> new ModelNotFoundException(version));
  }

  @Override
  public ModelDto getDefault() {
    OffsetDateTime createdAt = now();
    return ModelDto
        .builder()
        .name(DEFAULT_MODEL_NAME)
        .policy(getPolicyName())
        .createdAt(createdAt)
        .modelVersion(DigitsOnlyDateFormatter.INSTANCE.format(createdAt))
        .build();
  }

  @Override
  public ModelDto get(@NonNull String model) {
    return get(fromResourceName(model));
  }

  private String getPolicyName() {
    return currentPolicyProvider.getCurrentPolicy()
        .orElseThrow(() -> new ModelMisconfiguredException(DEFAULT_MODEL_NAME, "policyName"));
  }
}
