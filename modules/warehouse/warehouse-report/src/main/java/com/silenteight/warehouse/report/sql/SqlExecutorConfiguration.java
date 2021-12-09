package com.silenteight.warehouse.report.sql;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(SqlExecutorProperties.class)
class SqlExecutorConfiguration {

  @Bean
  SqlExecutorService sqlExecutor(
      @Valid SqlExecutorProperties sqlExecutorProperties,
      DataSource dataSource) {

    return new SqlExecutorService(sqlExecutorProperties, dataSource);
  }
}
