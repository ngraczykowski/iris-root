package com.silenteight.searpayments.bridge.dto.validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RequestMessageDtoValidatorConfiguration {

  @Bean
  RequestMessageDtoValidator requestMessageDtoValidator() {
    return new RequestMessageDtoValidatorImpl();
  }

}
