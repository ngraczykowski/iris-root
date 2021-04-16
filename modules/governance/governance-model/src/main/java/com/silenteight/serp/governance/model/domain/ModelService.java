package com.silenteight.serp.governance.model.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.model.domain.dto.AddModelRequest;
import com.silenteight.serp.governance.model.domain.exception.ModelAlreadyExistsException;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class ModelService {

  @NonNull
  private final ModelRepository modelRepository;

  @NonNull
  private final AuditingLogger auditingLogger;

  public UUID createModel(
      @NonNull UUID modelId,
      @NonNull String policyName,
      @NonNull String createdBy) {

    AddModelRequest request = AddModelRequest
        .builder()
        .modelId(modelId)
        .policyName(policyName)
        .createdBy(createdBy)
        .correlationId(randomUUID())
        .build();
    Model model = addModelInternal(request);
    return model.getModelId();
  }

  @NotNull
  Model addModelInternal(AddModelRequest request) {
    request.preAudit(auditingLogger::log);
    validateModelExistence(request.getPolicyName());
    Model model = new Model(request.getModelId(), request.getPolicyName());
    model = modelRepository.save(model);
    request.postAudit(auditingLogger::log);
    return model;
  }

  private void validateModelExistence(String policyName) {
    modelRepository
        .findByPolicyName(policyName)
        .ifPresent(s -> {
          throw new ModelAlreadyExistsException(s.getModelId(), policyName);
        });
  }
}
