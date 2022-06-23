package com.silenteight.serp.governance.file.validation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@RequiredArgsConstructor
class FileNameCharactersValidator implements FileValidator {

  @NonNull
  private final String allowedCharactersForFileName;

  @Override
  public boolean validate(MultipartFile file) {
    Pattern pattern = compile(allowedCharactersForFileName);
    String originalFilename = file.getOriginalFilename();
    Matcher matcher = pattern.matcher(originalFilename);
    return matcher.find();
  }
}
