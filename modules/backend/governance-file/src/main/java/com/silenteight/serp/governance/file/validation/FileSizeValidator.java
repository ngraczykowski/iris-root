package com.silenteight.serp.governance.file.validation;

import lombok.AllArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
class FileSizeValidator implements FileValidator {

  private final long maxFileSizeInBytes;

  @Override
  public boolean validate(MultipartFile file) {
    return file.getSize() <= maxFileSizeInBytes;
  }
}
