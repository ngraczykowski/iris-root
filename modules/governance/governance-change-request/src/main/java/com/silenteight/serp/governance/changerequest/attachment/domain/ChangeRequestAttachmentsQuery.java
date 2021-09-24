package com.silenteight.serp.governance.changerequest.attachment.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.attachment.list.ListAttachmentsQuery;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class ChangeRequestAttachmentsQuery implements ListAttachmentsQuery {

  @NonNull
  private final ChangeRequestAttachmentRepository repository;

  @Override
  public List<String> list(@NonNull UUID changeRequestId) {
    return repository.getAttachmentsList(changeRequestId);
  }
}
