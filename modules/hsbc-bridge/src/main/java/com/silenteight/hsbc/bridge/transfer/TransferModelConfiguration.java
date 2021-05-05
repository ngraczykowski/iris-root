package com.silenteight.hsbc.bridge.transfer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TransferModelConfiguration {

  @Bean
  ProcessManager transferModelProcessManager(
      ModelClient modelClient, TransferServiceClient transferServiceClient) {
    return new TransferModelProcessManager(modelClient, transferServiceClient);
  }
}
