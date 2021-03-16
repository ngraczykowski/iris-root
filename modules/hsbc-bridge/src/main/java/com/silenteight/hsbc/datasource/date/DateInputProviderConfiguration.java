package com.silenteight.hsbc.datasource.date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DateInputProviderConfiguration {

  @Bean
  DateInputProvider dateInputServiceProvider() {
    return new DateInputProvider();
  }
}
