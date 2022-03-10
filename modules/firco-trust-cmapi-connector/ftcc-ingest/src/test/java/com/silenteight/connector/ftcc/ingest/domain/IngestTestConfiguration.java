package com.silenteight.connector.ftcc.ingest.domain;

import com.silenteight.connector.ftcc.ingest.IngestModule;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    IngestModule.class
})
class IngestTestConfiguration {

  @MockBean
  private RegistrationServiceClient registrationServiceClient;
}
