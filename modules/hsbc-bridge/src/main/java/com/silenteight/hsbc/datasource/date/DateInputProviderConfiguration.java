package com.silenteight.hsbc.datasource.date;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class DateInputProviderConfiguration {

  private final MatchFacade matchFacade;

  @Bean
  DateInputProvider dateInputServiceProvider() {
    return new DateInputProvider(matchFacade);
  }
}
