package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.connector.ftcc.ingest.state.AlertStateEvaluator;
import com.silenteight.connector.ftcc.request.domain.MessageService;
import com.silenteight.connector.ftcc.request.domain.RequestService;
import com.silenteight.connector.ftcc.request.store.RequestStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IngestDomainConfiguration {

  @Bean
  IngestFacade ingestFacade(
      @NonNull RequestStorage requestStorage,
      @NonNull RegistrationApiClient registrationApiClient,
      @NonNull AlertStateEvaluator alertStateEvaluator,
      @NonNull DataPrepMessageGateway dataPrepMessageGateway) {

    return new IngestFacade(
        requestStorage,
        registrationApiClient,
        alertStateEvaluator,
        dataPrepMessageGateway);
  }

  @Bean
  RequestStorage requestStorage(
      @NonNull RequestService requestService, @NonNull MessageService messageService) {

    return new RequestStorage(requestService, messageService);
  }
}
