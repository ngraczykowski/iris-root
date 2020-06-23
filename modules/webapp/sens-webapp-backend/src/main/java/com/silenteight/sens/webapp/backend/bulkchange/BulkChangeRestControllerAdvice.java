package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.backend.reasoningbranch.list.exception.InvalidBranchIdException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class BulkChangeRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(InvalidBranchIdException.class)
  public ResponseEntity<ErrorDto> handle(InvalidBranchIdException e) {
    return handle(e, "Branch not found", NOT_FOUND);
  }
}
