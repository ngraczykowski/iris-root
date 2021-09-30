package com.silenteight.payments.bridge.common.dto.validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ValidatorConfiguration {

  @Bean
  RequestMessageDtoValidator requestMessageDtoValidator() {
    return new RequestMessageDtoValidatorImpl();
  }

  @Bean
  AlertMessageDtoValidator alertMessageDtoValidator() {
    return new AlertMessageDtoValidatorImpl();
  }
}
