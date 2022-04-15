package com.silenteight.serp.governance.file.validation;

import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
class FileNameLengthValidator implements FileValidator {

  private final long attachmentMaxNameLength;

  @Override
  public boolean validate(MultipartFile file) {
    return requireNonNull(file.getOriginalFilename()).length() <= attachmentMaxNameLength;
  }
}
