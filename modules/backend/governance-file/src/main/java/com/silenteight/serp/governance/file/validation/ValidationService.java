package com.silenteight.serp.governance.file.validation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.file.validation.exception.InvalidFileException;

import org.springframework.web.multipart.MultipartFile;

import static com.silenteight.serp.governance.file.validation.Error.FILE_NAME_TOO_LONG;
import static com.silenteight.serp.governance.file.validation.Error.FILE_SIZE_TOO_LARGE;
import static com.silenteight.serp.governance.file.validation.Error.INVALID_CONTENT;
import static com.silenteight.serp.governance.file.validation.Error.INVALID_FILE_NAME;
import static java.lang.String.format;

@RequiredArgsConstructor
public class ValidationService {

  @NonNull
  private final FileValidator nameCharactersValidator;
  @NonNull
  private final FileValidator mimeTypeValidator;
  @NonNull
  private final FileValidator nameLengthValidator;
  @NonNull
  private final FileValidator sizeValidator;

  public void validate(MultipartFile file) {
    if (!nameCharactersValidator.validate(file))
      throw new InvalidFileException(
          createExceptionMessage(file.getOriginalFilename()), INVALID_FILE_NAME);

    if (!mimeTypeValidator.validate(file))
      throw new InvalidFileException(
          createExceptionMessage(file.getOriginalFilename()), INVALID_CONTENT);

    if (!nameLengthValidator.validate(file))
      throw new InvalidFileException(
          createExceptionMessage(file.getOriginalFilename()), FILE_NAME_TOO_LONG);

    if (!sizeValidator.validate(file))
      throw new InvalidFileException(
          createExceptionMessage(file.getOriginalFilename()), FILE_SIZE_TOO_LARGE);
  }

  private static String createExceptionMessage(String fileName) {
    return format("File %s is not valid.", fileName);
  }
}
