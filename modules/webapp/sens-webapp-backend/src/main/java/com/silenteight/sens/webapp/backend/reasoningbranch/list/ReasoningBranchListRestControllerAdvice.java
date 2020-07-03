package com.silenteight.sens.webapp.backend.reasoningbranch.list;

import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;
import com.silenteight.sens.webapp.grpc.InvalidBranchSolutionException;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Order(ControllerAdviceOrder.BRANCH)
class ReasoningBranchListRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(InvalidBranchSolutionException.class)
  public ResponseEntity<ErrorDto> handle(InvalidBranchSolutionException e) {
    return handle(
        e,
        "InvalidBranchSolution",
        BAD_REQUEST,
        Map.of("solution", defaultString(e.getSolution(), "null")));
  }
}
