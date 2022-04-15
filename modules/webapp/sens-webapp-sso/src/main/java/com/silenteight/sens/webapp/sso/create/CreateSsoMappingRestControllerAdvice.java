package com.silenteight.sens.webapp.sso.create;

import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;
import com.silenteight.sep.usermanagement.api.identityprovider.exception.SsoRoleMapperAlreadyExistsException;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@ControllerAdvice
@Order(ControllerAdviceOrder.REPORT)
class CreateSsoMappingRestControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(SsoRoleMapperAlreadyExistsException.class)
  public ResponseEntity<ErrorDto> handle(SsoRoleMapperAlreadyExistsException e) {
    return handle(e, "Sso role mapper already exists.", BAD_REQUEST);
  }
}
