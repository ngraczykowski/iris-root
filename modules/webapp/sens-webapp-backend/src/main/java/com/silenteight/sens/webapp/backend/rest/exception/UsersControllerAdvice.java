package com.silenteight.sens.webapp.backend.rest.exception;

import com.silenteight.sens.webapp.backend.support.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.backend.support.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.backend.support.rest.exception.dto.ErrorDto;
import com.silenteight.sens.webapp.common.exception.EntityNotFoundException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(ControllerAdviceOrder.USERS)
public class UsersControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(EntityNotFoundException e) {
    return handle(e, "EntityNotFoundException", HttpStatus.NOT_FOUND);
  }
}
