package com.silenteight.serp.governance.changerequest.attachment.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.attachment.domain.ChangeRequestAttachmentsService;
import com.silenteight.serp.governance.file.domain.FileReferenceService;
import com.silenteight.serp.governance.file.storage.FileService;

import java.util.UUID;

import static com.silenteight.serp.governance.file.common.FileResource.validateFileResourceName;

@RequiredArgsConstructor
class DeleteAttachmentsUseCase {

  @NonNull
  FileService fileService;

  @NonNull
  ChangeRequestAttachmentsService changeRequestAttachmentsService;

  @NonNull
  FileReferenceService fileReferenceService;

  void deleteAttachments(UUID changeRequestId, String fileName) {
    validateFileResourceName(fileName);
    changeRequestAttachmentsService.deleteAttachments(changeRequestId, fileName);
    fileReferenceService.deleteFileReference(fileName);
    fileService.deleteFile(fileName);
  }
}
