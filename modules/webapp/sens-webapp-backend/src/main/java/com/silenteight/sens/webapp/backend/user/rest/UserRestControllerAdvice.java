package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.user.rest.UserRestController.UserRegistrationException;
import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator.UsernameNotUniqueError;

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
}
