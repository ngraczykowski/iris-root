package com.silenteight.connector.ftcc.ingest.domain;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(IngestDomainConfiguration.class)
class IngestTestConfiguration {

  @MockBean
  private RegistrationApiClient registrationApiClient;

  @MockBean
  private DataPrepMessageGateway dataPrepMessageGateway;
}
