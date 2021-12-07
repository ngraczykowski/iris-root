package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Component
public class TransformReaderFactory {

  private final DataSource dataSource;

  public <T> JdbcCursorItemReader<T> createReader(
      Long jobId, String query, RowMapper<T> rowMapper) {
    return new JdbcCursorItemReaderBuilder<T>()
        .name("flatRowsReader")
        .dataSource(dataSource)
        .queryArguments(jobId)
        .sql(query)
        .rowMapper(rowMapper)
        .build();
  }

  public <T> JdbcCursorItemReader<T> createReader(
      Class<T> mapType,
      Long jobId, String query) {
    return createReader(jobId, query, new BeanPropertyRowMapper<>(mapType));
  }
}
