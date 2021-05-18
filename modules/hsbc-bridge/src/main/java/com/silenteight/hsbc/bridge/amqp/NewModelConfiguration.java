package com.silenteight.hsbc.bridge.amqp;

import com.silenteight.hsbc.bridge.transfer.ProcessManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class NewModelConfiguration {

  @Profile("!dev")
  @Bean
  NewModelListener newModelListener(ProcessManager processManager) {
    return new NewModelListener(processManager);
  }
}
