package com.silenteight.hsbc.bridge.watchlist;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
class WorldCheckNotifierMockConfiguration {

  @Bean
  WorldCheckNotifier worldCheckNotifierMock() {
    return identifiers -> log.warn("Worldcheck notificaiton sent");
  }
}
