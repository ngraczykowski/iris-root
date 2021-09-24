package com.silenteight.serp.governance.file.validation;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidator {

  boolean validate(MultipartFile file);
}
