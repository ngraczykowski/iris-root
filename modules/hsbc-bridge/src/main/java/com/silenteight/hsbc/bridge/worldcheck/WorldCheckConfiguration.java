package com.silenteight.hsbc.bridge.worldcheck;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class WorldCheckConfiguration {

  @Bean
  @Profile("!dev")
  WorldCheckAdapter worldCheckAdapter() {
    return new WorldCheckAdapter();
  }

  @Bean
  @Profile("dev")
  WorldCheckServiceClientMock worldCheckServiceClientMock() {
    return new WorldCheckServiceClientMock();
  }
}
