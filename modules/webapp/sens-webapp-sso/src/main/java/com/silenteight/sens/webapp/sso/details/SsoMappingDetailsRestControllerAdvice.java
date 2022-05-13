package com.silenteight.sens.webapp.sso.details;

import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;
import com.silenteight.sep.usermanagement.api.identityprovider.exception.IdentityProviderNotFoundException;
import com.silenteight.sep.usermanagement.api.identityprovider.exception.SsoRoleMapperNotFoundException;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder.GLOBAL;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Order(GLOBAL)
class SsoMappingDetailsRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(SsoRoleMapperNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(SsoRoleMapperNotFoundException e) {
    return handle(e, "Sso role mapper not found.", NOT_FOUND);
  }

  @ExceptionHandler(IdentityProviderNotFoundException.class)
  public ResponseEntity<ErrorDto> handle(IdentityProviderNotFoundException e) {
    return handle(e, "No Identity Provider found.", NOT_FOUND);
  }
}
