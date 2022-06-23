package com.silenteight.serp.governance.file.upload;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.web.exception.AbstractErrorControllerAdvice;
import com.silenteight.serp.governance.common.web.exception.ErrorDto;
import com.silenteight.serp.governance.file.validation.exception.InvalidFileException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
class UploadControllerAdvice extends AbstractErrorControllerAdvice {

  private static final String ERROR = "error";

  @ExceptionHandler(InvalidFileException.class)
  public ResponseEntity<ErrorDto> handle(InvalidFileException e) {
    log.error("Error occurred", e);
    return handle(e, "File is not valid.", BAD_REQUEST, of(ERROR, e.getError()));
  }
}
