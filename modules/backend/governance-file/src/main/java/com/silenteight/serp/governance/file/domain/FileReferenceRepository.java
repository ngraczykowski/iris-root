package com.silenteight.serp.governance.file.domain;

import org.springframework.data.repository.Repository;

import java.util.UUID;

interface FileReferenceRepository extends Repository<FileReference, Long> {

  FileReference save(FileReference fileReference);

  FileReference getFileReferenceByFileId(UUID fileId);

  long deleteByFileId(UUID internalName);
}
