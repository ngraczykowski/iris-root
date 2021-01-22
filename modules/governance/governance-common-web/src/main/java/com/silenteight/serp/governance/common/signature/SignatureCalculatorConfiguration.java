package com.silenteight.serp.governance.common.signature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SignatureCalculatorConfiguration {

  @Bean
  SignatureCalculator signatureCalculator() {
    return new SignatureCalculator();
  }
}
