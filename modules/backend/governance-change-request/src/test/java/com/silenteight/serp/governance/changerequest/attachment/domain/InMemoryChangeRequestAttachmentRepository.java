package com.silenteight.serp.governance.changerequest.attachment.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

class InMemoryChangeRequestAttachmentRepository
    extends BasicInMemoryRepository<ChangeRequestAttachment>
    implements ChangeRequestAttachmentRepository {

  @Override
  public void removeChangeRequestAttachmentReferenceByFileName(String fileName) {
    stream()
        .filter(changeRequestAttachment -> changeRequestAttachment.getFileName().equals(fileName))
        .forEach(super::delete);
  }

  @Override
  public List<String> getAttachmentsList(UUID changeRequestId) {
    return stream()
        .filter(changeRequestAttachment -> changeRequestAttachment
            .getChangeRequestId().equals(changeRequestId))
        .map(ChangeRequestAttachment::getFileName)
        .collect(toList());
  }

  @Override
  public long countAllByChangeRequestId(UUID changeRequestId) {
    return getAttachmentsList(changeRequestId).size();
  }
}
