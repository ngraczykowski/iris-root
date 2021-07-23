package com.silenteight.serp.governance.model.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;
import com.silenteight.serp.governance.model.domain.dto.AddModelRequest;
import com.silenteight.serp.governance.model.domain.exception.ModelAlreadyExistsException;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import javax.transaction.Transactional;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class ModelService {

  @NonNull
  private final ModelRepository modelRepository;

  @NonNull
  private final AuditingLogger auditingLogger;

  @Transactional
  public UUID createModel(
      @NonNull UUID modelId,
      @NonNull String policy,
      @NonNull String createdBy) {

    AddModelRequest request = AddModelRequest.builder()
                                             .id(modelId)
                                             .policy(policy)
                                             .createdBy(createdBy)
                                             .correlationId(randomUUID())
                                             .build();
    Model model = addModelInternal(request);
    return model.getModelId();
  }

  @NotNull
  Model addModelInternal(AddModelRequest request) {
    request.preAudit(auditingLogger::log);
    validateModelExistence(request.getPolicy());
    Model model = new Model(request.getId(), request.getPolicy());
    model = modelRepository.save(model);
    request.postAudit(auditingLogger::log);
    return model;
  }

  private void validateModelExistence(String policyName) {
    modelRepository
        .findAllByPolicyName(policyName)
        .stream()
        .findAny()
        .ifPresent(model -> {
          throw new ModelAlreadyExistsException(model.getModelId(), policyName);
        });
  }

  @Transactional
  public UUID createModelWithModelVersion(
      UUID modelId, String policyName, String approvedBy, String modelVersion) {
    UUID result = createModel(modelId, policyName, approvedBy);
    setModelVersion(modelId, modelVersion);
    return result;
  }

  void setModelVersion(UUID modelId, String modelVersion) {
    modelRepository
        .findByModelId(modelId)
        .orElseThrow(EntityNotFoundException::new)
        .setModelVersion(modelVersion);
  }
}
