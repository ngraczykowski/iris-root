package com.silenteight.warehouse.production.persistence.partitioning;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class TablePartitionConfiguration {

  @Bean
  public MatchTablePartitionScheduler matchTablePartitionScheduler(
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      @Value("${warehouse.partition.size}") Integer size) {
    return new MatchTablePartitionScheduler(namedParameterJdbcTemplate, size);
  }
}
