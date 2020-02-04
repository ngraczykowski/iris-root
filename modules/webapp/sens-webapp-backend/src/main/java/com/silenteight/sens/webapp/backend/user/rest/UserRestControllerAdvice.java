package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.backend.support.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.backend.support.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.backend.user.registration.domain.UserRegistrationDomainError;
import com.silenteight.sens.webapp.backend.user.registration.domain.UsernameUniquenessValidator.UsernameNotUnique;
import com.silenteight.sens.webapp.backend.user.rest.UserRestController.UserRegistrationException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

@ControllerAdvice
@Order(ControllerAdviceOrder.RESTRICTION)
class UserRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(UserRegistrationException.class)
  public ResponseEntity<String> handle(UserRegistrationException e) {
    UserRegistrationDomainError error = e.getError();

    HttpStatus status = Match(error).of(
        Case($(instanceOf(UsernameNotUnique.class)), HttpStatus.CONFLICT),
        Case($(), () -> HttpStatus.UNPROCESSABLE_ENTITY)
    );

    return ResponseEntity.status(status).body(error.getReason());
  }
}
