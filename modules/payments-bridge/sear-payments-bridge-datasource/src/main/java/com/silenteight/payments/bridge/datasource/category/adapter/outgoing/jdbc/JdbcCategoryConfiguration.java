package com.silenteight.payments.bridge.datasource.category.adapter.outgoing.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcCategoryConfiguration {

  @Bean
  InsertCategoryValueQuery insertCategoryValueQuery(JdbcTemplate jdbcTemplate) {
    return new InsertCategoryValueQuery(jdbcTemplate);
  }

  @Bean
  SelectCategoryQuery selectCategoryQuery(JdbcTemplate jdbcTemplate) {
    return new SelectCategoryQuery(jdbcTemplate);
  }

}
