package com.silenteight.serp.governance.file.validation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.silenteight.serp.governance.file.common.MimeDetector.getMimeType;

@RequiredArgsConstructor
class FileMimeTypeValidator implements FileValidator {

  @NonNull
  private final List<String> allowedTypes;

  @Override
  public boolean validate(MultipartFile file) {
    final String mimeType = getMimeType(file);
    return allowedTypes.contains(mimeType);
  }
}
