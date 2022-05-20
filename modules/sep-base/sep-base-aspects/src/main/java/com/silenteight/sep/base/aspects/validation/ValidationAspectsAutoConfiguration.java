package com.silenteight.sep.base.aspects.validation;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.Aspects;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;

import javax.validation.Validator;

import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;

@ConditionalOnClass(name = "javax.validation.Validator")
@Configuration
@RequiredArgsConstructor
public class ValidationAspectsAutoConfiguration {

  public static final String VALIDATION_ASPECT_BEAN_NAME =
      "com.silenteight.serp.aspects.validation.validationAspect";

  @Bean(name = VALIDATION_ASPECT_BEAN_NAME)
  @Role(ROLE_INFRASTRUCTURE)
  public ValidationAspect validationAspect(@Lazy Validator validator) {
    ValidationAspect validationAspect = Aspects.aspectOf(ValidationAspect.class);
    validationAspect.setValidator(validator);
    return validationAspect;
  }
}
