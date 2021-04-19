package com.silenteight.serp.governance.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class ChangeRequestService {

  @NonNull
  private final ChangeRequestRepository changeRequestRepository;

  @NonNull
  private final AuditingLogger auditingLogger;

  public UUID addChangeRequest(
      @NonNull UUID changeRequestId,
      @NonNull String modelName,
      @NonNull String createdBy,
      @NonNull String creatorComment) {

    AddChangeRequestRequest request = AddChangeRequestRequest.builder()
        .correlationId(randomUUID())
        .changeRequestId(changeRequestId)
        .modelName(modelName)
        .createdBy(createdBy)
        .creatorComment(creatorComment)
        .build();
    ChangeRequest changeRequest = addChangeRequestInternal(request);
    return changeRequest.getChangeRequestId();
  }

  @NotNull
  ChangeRequest addChangeRequestInternal(AddChangeRequestRequest request) {
    request.preAudit(auditingLogger::log);
    ChangeRequest changeRequest = new ChangeRequest(
        request.getChangeRequestId(),
        request.getModelName(),
        request.getCreatedBy(),
        request.getCreatorComment());
    changeRequest = changeRequestRepository.save(changeRequest);
    request.postAudit(auditingLogger::log);
    return changeRequest;
  }
}
