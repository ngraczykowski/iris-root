package com.silenteight.serp.governance.qa.sampling.generator;

import com.silenteight.serp.governance.qa.send.DecisionCreatedHandler;
import com.silenteight.serp.governance.qa.send.SendAlertMessageUseCase;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    SamplingGeneratorConfiguration.class
})
class TestSamplingGeneratorConfiguration {

  @MockBean
  private SendAlertMessageUseCase sendAlertMessageUseCase;

  @Bean
  DecisionCreatedHandler decisionCreatedHandler() {
    return new DecisionCreatedHandler(sendAlertMessageUseCase);
  }
}

