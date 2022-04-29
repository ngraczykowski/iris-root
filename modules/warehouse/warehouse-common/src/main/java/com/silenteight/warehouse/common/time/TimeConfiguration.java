package com.silenteight.warehouse.common.time;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfiguration {

  @Bean
  TimeSource timeSource() {
    return DefaultTimeSource.INSTANCE;
  }
}
