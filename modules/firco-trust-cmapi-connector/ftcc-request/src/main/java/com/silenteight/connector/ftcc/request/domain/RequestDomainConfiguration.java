package com.silenteight.connector.ftcc.request.domain;

import lombok.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EntityScan
@EnableJpaRepositories
class RequestDomainConfiguration {

  @Bean
  RequestService requestService(@NonNull RequestRepository requestRepository) {
    return new RequestService(requestRepository);
  }

  @Bean
  MessageService messageService(@NonNull MessageRepository messageRepository) {
    return new MessageService(new MessageIdGenerator(), messageRepository);
  }

  @Bean
  MessageQuery messageQuery(
      @NonNull MessageRepository messageRepository,
      @NonNull NamedParameterJdbcTemplate jdbcTemplate,
      @NonNull ObjectMapper objectMapper) {

    return new MessageQuery(messageRepository, jdbcTemplate, objectMapper);
  }
}
