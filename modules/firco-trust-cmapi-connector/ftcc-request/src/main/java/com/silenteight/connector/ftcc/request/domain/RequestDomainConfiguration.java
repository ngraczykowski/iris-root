package com.silenteight.connector.ftcc.request.domain;

import lombok.NonNull;

import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.Clock;
import javax.validation.Valid;

@Configuration
@EntityScan
@EnableJpaRepositories
@EnableConfigurationProperties(RequestRetentionProperties.class)
class RequestDomainConfiguration {

  @Bean
  RequestService requestService(
      @NonNull RequestRepository requestRepository,
      @NonNull PartitionCreator partitionCreator,
      @NonNull Clock clock) {

    return new RequestService(requestRepository, partitionCreator, clock);
  }

  @Bean
  MessageService messageService(
      @NonNull MessageRepository messageRepository,
      @NonNull PartitionCreator partitionCreator,
      @NonNull Clock clock) {

    return new MessageService(new MessageIdGenerator(), messageRepository, partitionCreator, clock);
  }

  @Bean
  MessageQuery messageQuery(
      @NonNull MessageRepository messageRepository,
      @NonNull NamedParameterJdbcTemplate jdbcTemplate,
      @NonNull ObjectMapper objectMapper,
      @NonNull Clock clock,
      @NonNull @Valid RequestRetentionProperties requestRetentionProperties) {

    return new MessageQuery(
        messageRepository,
        jdbcTemplate,
        objectMapper,
        clock,
        requestRetentionProperties.getDataRetentionDuration());
  }
}
