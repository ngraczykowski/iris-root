package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.BranchIdsNotFoundException;
import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.FeatureVectorSignaturesNotFoundException;
import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.update.AiSolutionNotSupportedException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Order(ControllerAdviceOrder.BRANCH)
class ReasoningBranchRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(AiSolutionNotSupportedException.class)
  public ResponseEntity<ErrorDto> handle(AiSolutionNotSupportedException e) {
    return handle(e, "AiSolutionNotSupported", BAD_REQUEST);
  }

  @ExceptionHandler(BranchIdsNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(BranchIdsNotFoundException e) {
    return handle(
        e, "BranchIdsNotFound", BAD_REQUEST, Map.of("branchIds", e.getNonExistingBranchIds()));
  }

  @ExceptionHandler(FeatureVectorSignaturesNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(FeatureVectorSignaturesNotFoundException e) {
    return handle(
        e,
        "FeatureVectorSignaturesNotFound",
        BAD_REQUEST,
        Map.of("featureVectorSignatures", e.getNonExistingFeatureVectorSignatures()));
  }
}
