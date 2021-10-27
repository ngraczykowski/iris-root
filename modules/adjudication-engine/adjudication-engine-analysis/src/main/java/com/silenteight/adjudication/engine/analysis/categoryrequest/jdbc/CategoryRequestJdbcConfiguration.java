package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Getter
class CategoryRequestJdbcConfiguration {

  @Value("${ae.categories.missing.batch-size.select:4096}")
  private int selectBatchSize;

  @Bean
  SelectMissingMatchCategoryValuesQuery selectMissingMatchCategoryValuesQuery(
      ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {

    return new SelectMissingMatchCategoryValuesQuery(objectMapper, jdbcTemplate, selectBatchSize);
  }
}
