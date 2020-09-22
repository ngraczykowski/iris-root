package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.user.rest.UserRestController.UserRegistrationException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.user.remove.OriginNotMatchingException;
import com.silenteight.sens.webapp.user.remove.UserNotFoundException;
import com.silenteight.sep.usermanagement.api.UserDomainError;
import com.silenteight.sep.usermanagement.api.UsernameUniquenessValidator.UsernameNotUniqueError;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
@Order(ControllerAdviceOrder.RESTRICTION)
class UserRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(UserRegistrationException.class)
  public ResponseEntity<String> handle(UserRegistrationException e) {
    UserDomainError error = e.getError();

    HttpStatus status = Match(error).of(
        Case($(instanceOf(UsernameNotUniqueError.class)), CONFLICT),
        Case($(), () -> UNPROCESSABLE_ENTITY));

    return ResponseEntity.status(status).body(error.getReason());
  }

  @ExceptionHandler(OriginNotMatchingException.class)
  public ResponseEntity<String> handle(OriginNotMatchingException e) {
    return ResponseEntity.status(CONFLICT).body("The user cannot be deleted via API");
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handle(UserNotFoundException e) {
    return ResponseEntity.status(NOT_FOUND).body("The user cannot be found");
  }
}
