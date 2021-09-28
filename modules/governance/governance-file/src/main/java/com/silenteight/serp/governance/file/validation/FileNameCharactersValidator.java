package com.silenteight.serp.governance.file.validation;

import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

class FileNameCharactersValidator implements FileValidator {

  private static final Pattern PATTERN = compile("^[._a-zA-Z0-9-()]*$");

  @Override
  public boolean validate(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    Matcher matcher = PATTERN.matcher(originalFilename);
    return matcher.find();
  }
}
