package com.silenteight.serp.governance.changerequest.attachment.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.file.description.FileDescriptionQuery;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;
import com.silenteight.serp.governance.file.storage.FileService;
import com.silenteight.serp.governance.file.storage.FileWrapper;

import static com.silenteight.serp.governance.file.common.FileResource.validateFileResourceName;

@RequiredArgsConstructor
class DownloadAttachmentsUseCase {

  @NonNull
  private final FileDescriptionQuery fileDescriptionQuery;
  @NonNull
  private final FileService fileService;

  FileWrapper activate(String fileName) {
    validateFileResourceName(fileName);
    FileReferenceDto fileReferenceDto = fileDescriptionQuery.get(fileName);
    byte[] file = fileService.getFile(fileName);

    return FileWrapper.builder()
        .fileName(fileReferenceDto.getFileName())
        .mimeType(fileReferenceDto.getMimeType())
        .content(file)
        .build();
  }
}
