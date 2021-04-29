package com.silenteight.hsbc.bridge.transfer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TransferModelConfiguration {

  @Bean
  ProcessManager transferModelProcessManager(
      ModelClient modelClient, TransferClient transferClient) {
    return new TransferModelProcessManager(modelClient, transferClient);
  }
}
