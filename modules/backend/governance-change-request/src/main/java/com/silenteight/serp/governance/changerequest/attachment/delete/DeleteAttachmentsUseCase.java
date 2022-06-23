package com.silenteight.serp.governance.changerequest.attachment.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.attachment.domain.ChangeRequestAttachmentsService;
import com.silenteight.serp.governance.file.domain.FileReferenceService;
import com.silenteight.serp.governance.file.storage.FileService;

import java.util.UUID;

@RequiredArgsConstructor
class DeleteAttachmentsUseCase {

  @NonNull
  FileService fileService;

  @NonNull
  ChangeRequestAttachmentsService changeRequestAttachmentsService;

  @NonNull
  FileReferenceService fileReferenceService;

  void deleteAttachments(UUID changeRequestId, String fileName) {
    changeRequestAttachmentsService.deleteAttachments(changeRequestId, fileName);
    fileReferenceService.deleteFileReference(fileName);
    fileService.deleteFile(fileName);
  }
}
