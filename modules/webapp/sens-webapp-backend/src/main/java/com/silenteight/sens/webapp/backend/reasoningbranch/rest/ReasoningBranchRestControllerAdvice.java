package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchesNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Order(ControllerAdviceOrder.BRANCH)
class ReasoningBranchRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(AiSolutionNotSupportedException.class)
  public ResponseEntity<ErrorDto> handle(AiSolutionNotSupportedException e) {
    return handle(e, "AI Solution not supported", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BranchesNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(BranchesNotFoundException e) {
    return handle(
        e, "Branch(es) not found", HttpStatus.BAD_REQUEST,
        Map.of("branchIds", e.getNonExistingBranchIds()));
  }
}
