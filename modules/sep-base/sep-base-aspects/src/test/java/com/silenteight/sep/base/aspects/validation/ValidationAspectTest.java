package com.silenteight.sep.base.aspects.validation;

import com.silenteight.sep.base.aspects.validation.business.BusinessService;
import com.silenteight.sep.base.aspects.validation.business.model.BusinessObject;
import com.silenteight.sep.base.aspects.validation.business.model.BusinessRequest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import static org.assertj.core.api.Assertions.*;

class ValidationAspectTest {

  BusinessService testService = new BusinessService();

  @Test
  void validatesWhenCallMethodWithIncorrectParameterValues() {
    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> testService.businessMethod(new BusinessObject("text"), "foobar"))
        .withMessageContaining("text: size must be between 0 and 4");
  }

  @Test
  void validatesReturnedMethodResult() {
    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> testService.createRequest(300, "short text", ""))
        .withMessageContaining("integer: must be less than or equal to 255")
        .withMessageContaining("requiredText: must not be blank");
  }

  @Test
  void validatesParametersDuringNewObjectCreation() {
    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> new BusinessObject("not blank", 20))
        .withMessageContaining("repeat: must be less than or equal to 10");
  }

  @Test
  void validatesReturnedObjectDuringNewObjectCreation() {
    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> new BusinessObject("text_1", 10))
        .withMessageContaining("resultText: size must be between 0 and 50");
  }

  @Test
  void validatesReturnedObject() {
    assertThat(new BusinessObject("suffix_", "text"))
        .hasFieldOrPropertyWithValue("resultText", "suffix_text");
  }

  @Test
  void validatesMethodReturnedObject() {
    assertThat(validatedMethodReturningValidatedObject("not blank"))
        .hasFieldOrPropertyWithValue("resultText", "not blank");

    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> validatedMethodReturningValidatedObject(""))
        .withMessageContaining("resultText: must not be blank");
  }

  @Test
  void validatesUsingHibernateConstraint() {
    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> testService.businessMethod("8501011234"))
        .withMessageContaining("invalid Polish National Identification Number (PESEL)");
  }

  @Test
  void validatesWhenCallMethodWithValidatedParameters() {
    Assertions.assertThat(testService.performLogic(
        BusinessRequest.builder().requiredText("text").shortText("short").integer(1).build()))
        .hasFieldOrPropertyWithValue("resultText", "text/short@1");

    assertThatExceptionOfType(ConstraintViolationException.class)
        .isThrownBy(() -> testService.performLogic(BusinessRequest.builder().build()))
        .withMessageContaining("requiredText: must not be null");
  }

  @Test
  void staticMethodValidationIsUnsupported() {
    BusinessRequest request = BusinessRequest
        .builder()
        .requiredText("text")
        .shortText("short")
        .integer(1)
        .build();

    assertThatExceptionOfType(UnsupportedStaticMethodException.class)
        .isThrownBy(() -> BusinessService.businessStaticMethod(request));
  }

  @Valid
  private BusinessObject validatedMethodReturningValidatedObject(String text) {
    return new BusinessObject(text);
  }
}
