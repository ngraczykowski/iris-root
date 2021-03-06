package com.silenteight.adjudication.engine.mock.governance;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mockgovernance")
class GrpcApiMockConfiguration {

  @Bean
  MockFeatureVectorSolutionReduction mockFeatureVectorSolutionReduction() {
    return new MockFeatureVectorSolutionReduction();
  }
}
