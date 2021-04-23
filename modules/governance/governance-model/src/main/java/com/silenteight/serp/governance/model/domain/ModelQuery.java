package com.silenteight.serp.governance.model.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.model.domain.exception.ModelNotFoundException;
import com.silenteight.serp.governance.model.get.GetModelDetailsQuery;
import com.silenteight.serp.governance.model.provide.grpc.DefaultModelQuery;
import com.silenteight.serp.governance.model.provide.grpc.SolvingModelDetailsQuery;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;

import java.util.UUID;

import static java.time.OffsetDateTime.now;
import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.substringAfter;

@RequiredArgsConstructor
public class ModelQuery
    implements GetModelDetailsQuery, DefaultModelQuery, SolvingModelDetailsQuery {

  @NonNull
  private final ModelRepository modelRepository;

  @NonNull
  private final CurrentPolicyProvider currentPolicyProvider;

  private static final String MODEL_NAME_RESOURCE_PREFIX = "models/";
  private static final String DEFAULT_MODEL_NAME = "models/default";

  public ModelDto get(@NonNull UUID modelId) {
    return modelRepository
        .findByModelId(modelId)
        .map(Model::toDto)
        .orElseThrow(() -> new ModelNotFoundException(modelId));
  }

  public ModelDto get(@NonNull String model) {
    return get(retrieveModelId(model));
  }

  public ModelDto getDefault() {
    return ModelDto.builder()
        .name(DEFAULT_MODEL_NAME)
        .policyName(getPolicyName())
        .createdAt(now())
        .build();
  }

  private static UUID retrieveModelId(String modelId) {
    return fromString(substringAfter(modelId, MODEL_NAME_RESOURCE_PREFIX));
  }

  private String getPolicyName() {
    return currentPolicyProvider.getCurrentPolicy()
        .orElseThrow(() -> new ModelMisconfiguredException(DEFAULT_MODEL_NAME, "policyName"));
  }
}
