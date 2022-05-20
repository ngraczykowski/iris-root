package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
class JdbcCategoryConfiguration {

  @Bean
  InsertCategoryValueQuery insertCategoryValueQuery(JdbcTemplate jdbcTemplate) {
    return new InsertCategoryValueQuery(jdbcTemplate);
  }

  @Bean
  InsertCategoriesQuery insertCategoriesQuery(JdbcTemplate jdbcTemplate) {
    return new InsertCategoriesQuery(jdbcTemplate);
  }

  @Bean
  SelectCategoryValueQuery selectCategoryValueQuery(JdbcTemplate jdbcTemplate) {
    return new SelectCategoryValueQuery(jdbcTemplate);
  }

  @Bean
  SelectCategoryQuery selectCategoryQuery(JdbcTemplate jdbcTemplate) {
    return new SelectCategoryQuery(jdbcTemplate);
  }

}
