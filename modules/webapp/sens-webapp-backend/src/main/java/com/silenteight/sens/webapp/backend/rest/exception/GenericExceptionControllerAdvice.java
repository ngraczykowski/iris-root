package com.silenteight.sens.webapp.backend.rest.exception;

import com.silenteight.sens.webapp.common.rest.exception.AbstractErrorControllerAdvice;
import com.silenteight.sens.webapp.common.rest.exception.ControllerAdviceOrder;
import com.silenteight.sens.webapp.common.rest.exception.dto.ErrorDto;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@Order(ControllerAdviceOrder.GLOBAL)
public class GenericExceptionControllerAdvice extends AbstractErrorControllerAdvice {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDto> handle(AccessDeniedException e) {
    return handle(e, "Access is denied", HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handle(MethodArgumentNotValidException e) {
    Map<String, Object> extras = new HashMap<>();
    extras.put("errors", errorDescriptionsOf(e));
    return handle(e, "InvalidMethodArguments", HttpStatus.BAD_REQUEST, extras);
  }

  private Stream<String> errorDescriptionsOf(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    Stream<String> globalErrors = bindingResult
        .getGlobalErrors()
        .stream()
        .map(ObjectError::getDefaultMessage);

    Stream<String> fieldErrors =
        bindingResult.getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage());

    return Stream.concat(globalErrors, fieldErrors);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handle(MissingServletRequestParameterException e) {
    Map<String, Object> extras = new HashMap<>();
    extras.put("parameterName", e.getParameterName());
    extras.put("parameterType", e.getParameterType());

    return handle(e, "MissingRequestParameter", HttpStatus.BAD_REQUEST, extras);
  }

  @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handle(UnsatisfiedServletRequestParameterException e) {
    Map<String, Object> extras = new HashMap<>();
    extras.put("actualParams", e.getActualParams());
    extras.put("paramConditions", e.getParamConditionGroups());

    return handle(e, "UnsatisfiedRequestParameter", HttpStatus.BAD_REQUEST, extras);
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<ErrorDto> handle(MissingPathVariableException e) {
    Map<String, Object> extras = new HashMap<>();
    extras.put("parameter", e.getParameter());
    extras.put("variableName", e.getVariableName());

    return handle(e, "MissingPathVariable", HttpStatus.BAD_REQUEST, extras);
  }

  @ExceptionHandler(ServletRequestBindingException.class)
  public ResponseEntity<ErrorDto> handle(ServletRequestBindingException e) {
    return handle(e, "RequestBindingException", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
      EntityNotFoundException.class,
      com.silenteight.sens.webapp.common.exception.EntityNotFoundException.class
  })
  public ResponseEntity<ErrorDto> handle(Exception e) {
    return handle(e, "EntityNotFoundException", HttpStatus.NOT_FOUND);
  }
}
