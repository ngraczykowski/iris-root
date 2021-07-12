package com.silenteight.serp.governance.qa.send;

import com.silenteight.serp.governance.qa.send.amqp.AlertMessageGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SendAlertMessageConfiguration {

  @Bean
  SendAlertMessageUseCase sendAlertMessageUseCase(AlertMessageGateway alertMessageGateway) {
    return new SendAlertMessageUseCase(alertMessageGateway);
  }

  @Bean
  DecisionCreatedHandler decisionCreatedHandler(SendAlertMessageUseCase sendAlertMessageUseCase) {
    return new DecisionCreatedHandler(sendAlertMessageUseCase);
  }
}
