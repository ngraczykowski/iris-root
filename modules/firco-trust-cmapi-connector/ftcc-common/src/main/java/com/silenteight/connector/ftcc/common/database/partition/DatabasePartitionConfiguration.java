package com.silenteight.connector.ftcc.common.database.partition;

import lombok.NonNull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;

import static java.time.Clock.systemUTC;

@Configuration
@EnableTransactionManagement
@EnableScheduling
public class DatabasePartitionConfiguration {

  @Bean
  PartitionCreator partitionCreator(@NonNull NamedParameterJdbcTemplate jdbcTemplate) {
    return new PartitionCreator(jdbcTemplate);
  }

  @Bean
  Clock clock() {
    return systemUTC();
  }
}
