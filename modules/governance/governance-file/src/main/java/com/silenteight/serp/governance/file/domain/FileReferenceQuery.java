package com.silenteight.serp.governance.file.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.file.description.FileDescriptionQuery;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

import org.springframework.beans.factory.annotation.Autowired;

import static com.silenteight.serp.governance.file.common.FileResource.fromResourceName;

@Slf4j
@AllArgsConstructor
class FileReferenceQuery implements FileDescriptionQuery {

  @Autowired
  FileReferenceRepository repository;

  @Override
  public FileReferenceDto get(String id) {
    FileReference fileReference = repository.getFileReferenceByFileId(fromResourceName(id));

    log.debug("Description request for file with id {} processed.", id);
    return fileReference.toDto();
  }
}
