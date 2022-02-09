package com.silenteight.serp.governance.changerequest.attachment.add;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.attachment.domain.ChangeRequestAttachmentsService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class AddAttachmentsUseCase {

  @NonNull
  private final ChangeRequestAttachmentsService changeRequestAttachmentsService;

  void addAttachments(UUID changeRequestId, List<String> attachmentsList) {
    changeRequestAttachmentsService.addAttachments(changeRequestId, attachmentsList);
  }
}
