package com.silenteight.serp.governance.changerequest.attachment.add;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.attachment.domain.ChangeRequestAttachmentsService;
import com.silenteight.serp.governance.file.common.FileResource;
import com.silenteight.serp.governance.file.common.exception.WrongFilesResourceFormatException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class AddAttachmentsUseCase {

  @NonNull
  private final ChangeRequestAttachmentsService changeRequestAttachmentsService;

  void addAttachments(UUID changeRequestId, List<String> attachmentsList) {
    validateAttachments(attachmentsList);
    changeRequestAttachmentsService.addAttachments(changeRequestId, attachmentsList);
  }

  private static void validateAttachments(List<String> attachmentsList) {
    if (!allNamesAreValid(attachmentsList))
      throw new WrongFilesResourceFormatException(attachmentsList);
  }

  private static boolean allNamesAreValid(List<String> attachmentsList) {
    return attachmentsList
        .stream()
        .allMatch(FileResource::isNameValid);
  }
}
