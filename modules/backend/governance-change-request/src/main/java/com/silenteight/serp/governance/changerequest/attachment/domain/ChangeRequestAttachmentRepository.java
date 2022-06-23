package com.silenteight.serp.governance.changerequest.attachment.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

interface ChangeRequestAttachmentRepository
    extends Repository<ChangeRequestAttachment, Long> {

  ChangeRequestAttachment save(ChangeRequestAttachment changeRequestAttachment);

  void removeChangeRequestAttachmentReferenceByFileName(String fileName);

  @Query(value = "SELECT cr.file_name "
      + "FROM governance_change_request_attachment cr "
      + "WHERE cr.change_request_id = :changeRequestId "
      + "ORDER BY cr.created_at DESC ", nativeQuery = true)
  List<String> getAttachmentsList(UUID changeRequestId);

  long countAllByChangeRequestId(UUID changeRequestId);
}
