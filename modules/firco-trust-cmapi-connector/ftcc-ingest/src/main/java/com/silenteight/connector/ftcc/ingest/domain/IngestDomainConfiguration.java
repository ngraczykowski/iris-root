package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class IngestDomainConfiguration {

  @Bean
  IngestFacade ingestFacade(
      @NonNull RequestStorage requestStorage,
      @NonNull RegistrationApiClient registrationApiClient,
      @NonNull DataPrepMessageGateway dataPrepMessageGateway) {

    return new IngestFacade(
        new BatchIdGenerator(),
        requestStorage,
        registrationApiClient,
        dataPrepMessageGateway);
  }

  @Bean
  RequestStorage requestStorage(
      @NonNull RequestService requestService, @NonNull MessageService messageService) {

    return new RequestStorage(requestService, messageService);
  }

  @Bean
  RequestService requestService(@NonNull RequestRepository requestRepository) {
    return new RequestService(requestRepository);
  }

  @Bean
  MessageService messageService(@NonNull MessageRepository messageRepository) {
    return new MessageService(new MessageIdGenerator(), messageRepository);
  }

  @Bean
  MessageQuery messageQuery(@NonNull MessageRepository messageRepository) {
    return new MessageQuery(messageRepository);
  }
}
