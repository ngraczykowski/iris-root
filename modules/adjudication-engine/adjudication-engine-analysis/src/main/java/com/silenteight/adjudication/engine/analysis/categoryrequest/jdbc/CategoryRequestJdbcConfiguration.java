package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Getter
class CategoryRequestJdbcConfiguration {

  /**
   * @deprecated Unused.
   */
  @Value("${ae.categories.missing.batch-size.insert:1024}")
  @Deprecated(since = "1.3.0")
  private int insertBatchSize;

  @Value("${ae.categories.missing.batch-size.select:4096}")
  private int selectBatchSize;

  @Bean
  SelectMissingMatchCategoryValuesQuery selectMissingMatchCategoryValuesQuery(
      DataSource dataSource) {

    return new SelectMissingMatchCategoryValuesQuery(dataSource, selectBatchSize);
  }
}
