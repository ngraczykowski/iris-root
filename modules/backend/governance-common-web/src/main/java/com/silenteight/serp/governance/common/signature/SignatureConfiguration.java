package com.silenteight.serp.governance.common.signature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SignatureConfiguration {

  @Bean
  SignatureCalculator signatureCalculator() {
    return new SignatureCalculator();
  }

  @Bean
  CanonicalFeatureVectorFactory canonicalFeatureVectorFactory(
      SignatureCalculator signatureCalculator) {
    return new CanonicalFeatureVectorFactory(signatureCalculator);
  }

}
