package com.silenteight.connector.ftcc.request.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RequestDomainConfiguration.class)
class RequestDomainTestConfiguration {

  @MockBean
  private ObjectMapper objectMapper;
}
