package com.silenteight.payments.bridge.firco.adapter.incoming.dto.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.adapter.incoming.dto.input.AlertMessageDto;

import org.jboss.logging.MDC;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

@Slf4j
@RequiredArgsConstructor
class AlertMessageDtoValidatorImpl implements AlertMessageDtoValidator {

  public void validate(
      @NonNull AlertMessageDto messageDto,
      @NonNull Class<? extends ValidationGroup> validationDefinitionClass) {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    var systemId = messageDto.getSystemID();
    var constraintViolations =
        validator.validate(messageDto, validationDefinitionClass);

    if (!constraintViolations.isEmpty()) {
      for (var constraintViolation : constraintViolations) {
        logConstraintViolation(constraintViolation, systemId);
      }
      throw new ValidationException("I failed to validate message " + systemId +
          ". Check log files above for detailed violation description.");
    }
  }

  private void logConstraintViolation(
      ConstraintViolation<AlertMessageDto> constraintViolation, String systemId) {
    MDC.put("systemId", systemId);
    try {
      log.warn(
          "Alert is damaged. Property {} violated {}.",
          constraintViolation.getPropertyPath(),
          constraintViolation.getMessage());
    } finally {
      MDC.remove("systemId");
    }
  }
}
