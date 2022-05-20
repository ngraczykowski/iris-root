package com.silenteight.warehouse.report.storage.temporary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TemporaryFileConfiguration {

  @Bean
  TemporaryFileService temporaryFileService() {
    return new TemporaryFileService();
  }
}
