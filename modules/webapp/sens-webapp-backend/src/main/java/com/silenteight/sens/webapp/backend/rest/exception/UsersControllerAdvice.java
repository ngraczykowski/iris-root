package com.silenteight.sens.webapp.backend.rest.exception;

import com.silenteight.sens.webapp.backend.rest.exception.dto.ErrorDto;
import com.silenteight.sens.webapp.common.exception.EntityNotFoundException;
import com.silenteight.sens.webapp.users.user.exception.UserAlreadyExistException;
import com.silenteight.sens.webapp.users.user.exception.UserIsUsedInWorkflowException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(ControllerAdviceOrder.USERS)
public class UsersControllerAdvice extends ErrorControllerAdvice {

  @ExceptionHandler(UserAlreadyExistException.class)
  public ResponseEntity<ErrorDto> handle(UserAlreadyExistException e) {
    return handle(e, "UserAlreadyExistsException", HttpStatus.CONFLICT);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(EntityNotFoundException e) {
    return handle(e, "EntityNotFoundException", HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserIsUsedInWorkflowException.class)
  public ResponseEntity<ErrorDto> handle(UserIsUsedInWorkflowException e) {
    return handle(
        e, "UserIsUsedInWorkflowException", HttpStatus.UNPROCESSABLE_ENTITY, e.getErrorMap());
  }
}
