package com.silenteight.adjudication.engine.solve.category.data;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Configuration
class CategoryDataAccessConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  InsertCategoryBatchSqlUpdate insertCategoryBatchSqlUpdate() {
    return new InsertCategoryBatchSqlUpdate(jdbcTemplate);
  }
}
