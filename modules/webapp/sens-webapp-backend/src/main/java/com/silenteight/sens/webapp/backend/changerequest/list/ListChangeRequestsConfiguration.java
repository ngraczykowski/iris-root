package com.silenteight.sens.webapp.backend.changerequest.list;

import com.silenteight.sens.webapp.backend.changerequest.rest.ChangeRequestQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ListChangeRequestsConfiguration {

  @Bean
  ChangeRequestQuery changeRequestQuery() {
    return new InMemoryChangeRequestQuery();
  }
}
