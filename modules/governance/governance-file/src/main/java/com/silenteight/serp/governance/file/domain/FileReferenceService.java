package com.silenteight.serp.governance.file.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.file.common.FileResource;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class FileReferenceService {

  @NonNull
  private final FileReferenceRepository repository;

  public FileReferenceDto saveFileReference(
      UUID fileId, String uploaderName, String originalFileName, Long fileSize, String mimeType) {

    log.info("Saving file reference for fileName={}",originalFileName);

    FileReference fileReference = new FileReference(
        fileId, originalFileName, uploaderName, fileSize, mimeType);
    return repository.save(fileReference).toDto();
  }

  @Transactional
  public void deleteFileReference(String fileName) {
    log.info("Deleting file reference for fileName={}",fileName);
    repository.deleteByFileId(FileResource.fromResourceName(fileName));
  }
}
