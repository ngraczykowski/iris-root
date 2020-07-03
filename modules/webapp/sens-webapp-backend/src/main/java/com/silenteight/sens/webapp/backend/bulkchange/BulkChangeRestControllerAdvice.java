package com.silenteight.sens.webapp.backend.bulkchange;

import com.silenteight.sens.webapp.backend.parser.exception.InvalidReasoningBranchIdException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class BulkChangeRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(InvalidReasoningBranchIdException.class)
  public ResponseEntity<ErrorDto> handle(InvalidReasoningBranchIdException e) {
    return handle(e, "InvalidReasoningBranchId", BAD_REQUEST, Map.of("branchId", e.getBranchId()));
  }
}
