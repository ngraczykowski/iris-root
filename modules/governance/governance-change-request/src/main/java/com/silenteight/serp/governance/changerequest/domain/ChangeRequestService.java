package com.silenteight.serp.governance.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.changerequest.domain.exception.ChangeRequestNotFoundException;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
public class ChangeRequestService {

  @NonNull
  private final ChangeRequestRepository repository;

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

  private ChangeRequest addChangeRequestInternal(AddChangeRequestRequest request) {
    request.preAudit(auditingLogger::log);
    ChangeRequest changeRequest = new ChangeRequest(
        request.getChangeRequestId(),
        request.getModelName(),
        request.getCreatedBy(),
        request.getCreatorComment());
    changeRequest = repository.save(changeRequest);
    request.postAudit(auditingLogger::log);
    return changeRequest;
  }

  public void approve(
      @NonNull UUID changeRequestId,
      @NonNull String approvedBy,
      @NonNull String approverComment) {

    ApproveChangeRequestRequest request = ApproveChangeRequestRequest.builder()
        .correlationId(randomUUID())
        .changeRequestId(changeRequestId)
        .approvedBy(approvedBy)
        .approverComment(approverComment)
        .build();
    approveInternal(request);
  }

  private void approveInternal(ApproveChangeRequestRequest request) {
    request.preAudit(auditingLogger::log);
    ChangeRequest changeRequest = getByChangeRequestId(request.getChangeRequestId());
    changeRequest.approve(request.getApprovedBy(), request.getApproverComment());
    repository.save(changeRequest);
    request.postAudit(auditingLogger::log);
  }

  public void reject(
      @NonNull UUID changeRequestId,
      @NonNull String rejectedBy,
      @NonNull String rejectorComment) {

    RejectChangeRequestRequest request = RejectChangeRequestRequest.builder()
        .correlationId(randomUUID())
        .changeRequestId(changeRequestId)
        .rejectedBy(rejectedBy)
        .rejectorComment(rejectorComment)
        .build();
    rejectInternal(request);
  }

  private void rejectInternal(RejectChangeRequestRequest request) {
    request.preAudit(auditingLogger::log);
    ChangeRequest changeRequest = getByChangeRequestId(request.getChangeRequestId());
    changeRequest.reject(request.getRejectedBy(), request.getRejectorComment());
    repository.save(changeRequest);
    request.postAudit(auditingLogger::log);
  }

  private ChangeRequest getByChangeRequestId(@NonNull UUID changeRequestId) {
    return repository
        .findByChangeRequestId(changeRequestId)
        .orElseThrow(() -> new ChangeRequestNotFoundException(changeRequestId));
  }
}
