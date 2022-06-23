package com.silenteight.serp.governance.changerequest.attachment.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestService;
import com.silenteight.serp.governance.changerequest.domain.ChangeRequestState;
import com.silenteight.serp.governance.changerequest.domain.exception.ChangeRequestNotInPendingStateException;
import com.silenteight.serp.governance.changerequest.domain.exception.MaxAttachmentsPerChangeRequestException;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;

@Slf4j
@RequiredArgsConstructor
public class ChangeRequestAttachmentsService {

  @NonNull
  private final ChangeRequestAttachmentRepository repository;

  @NonNull
  private final ChangeRequestService changeRequestService;

  private static final int MAX_NUMBER_OF_ATTACHMENTS_PER_CHANGE_REQUEST = 20;

  public void addAttachments(UUID changeRequestId, List<String> attachments) {
    validate(changeRequestId);

    attachments.stream()
               .map(file -> convertToReference(changeRequestId, file))
               .forEach(this::saveAttachment);

    log.debug("Add attachments for changeRequest {} processed, attachments {}",
              changeRequestId, attachments);
  }

  private void validate(UUID changeRequestId) {
    ChangeRequestState changeRequestState = changeRequestService.getChangeRequestState(
        changeRequestId);

    if (changeRequestState != PENDING)
      throw new ChangeRequestNotInPendingStateException(changeRequestId);

    long attachmentsByChangeRequestId = repository.countAllByChangeRequestId(changeRequestId);

    if (attachmentsByChangeRequestId >= MAX_NUMBER_OF_ATTACHMENTS_PER_CHANGE_REQUEST)
      throw new MaxAttachmentsPerChangeRequestException(changeRequestId);
  }

  private static ChangeRequestAttachment convertToReference(UUID changeRequestId, String fileName) {
    ChangeRequestAttachment changeRequestAttachment = new ChangeRequestAttachment();

    changeRequestAttachment.setChangeRequestId(changeRequestId);
    changeRequestAttachment.setFileName(fileName);
    return changeRequestAttachment;
  }

  private void saveAttachment(ChangeRequestAttachment changeRequestAttachment) {
    repository.save(changeRequestAttachment);
  }

  @Transactional
  public void deleteAttachments(UUID changeRequestId, String fileName) {
    ChangeRequestState changeRequestState =
        changeRequestService.getChangeRequestState(changeRequestId);

    if (changeRequestState != PENDING)
      throw new ChangeRequestNotInPendingStateException(changeRequestId);

    repository.removeChangeRequestAttachmentReferenceByFileName(fileName);
    log.debug("Delete attachment for changeRequest {} processed, fileName {}", changeRequestId,
              fileName);
  }
}
