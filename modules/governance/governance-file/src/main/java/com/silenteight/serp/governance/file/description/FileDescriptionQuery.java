package com.silenteight.serp.governance.file.description;

import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

public interface FileDescriptionQuery {

  FileReferenceDto get(String fileName);
}
