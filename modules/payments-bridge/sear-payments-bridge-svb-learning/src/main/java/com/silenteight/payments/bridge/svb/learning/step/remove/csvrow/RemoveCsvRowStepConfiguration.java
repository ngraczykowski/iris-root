package com.silenteight.payments.bridge.svb.learning.step.remove.csvrow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobProperties;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobConstants.REMOVE_FILE_DATA_STEP_NAME;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(RemoveFileDataJobProperties.class)
class RemoveCsvRowStepConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT learning_csv_row_id FROM pb_learning_csv_row WHERE file_name = ?";

  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource;
  private final RemoveFileDataJobProperties properties;
  private final ItemWriter<Long> removeCsvRowDataWriter;

  @Bean
  Step removeCsvRowsStep(JdbcCursorItemReader<Long> removeLearningCsvRowReader) {
    return stepBuilderFactory
        .get(REMOVE_FILE_DATA_STEP_NAME)
        .<Long, Long>chunk(properties.getChunkSize())
        .reader(removeLearningCsvRowReader)
        .writer(removeCsvRowDataWriter)
        .build();
  }

  @Bean
  @StepScope
  public JdbcCursorItemReader<Long> removeLearningCsvRowReader(
      @Value("#{stepExecution}") StepExecution stepExecution) {
    var fileName = stepExecution.getJobParameters().getString(FILE_NAME_PARAMETER);
    return new JdbcCursorItemReaderBuilder<Long>()
        .name("csvRowIdReader")
        .dataSource(dataSource)
        .queryArguments(fileName)
        .sql(QUERY)
        .rowMapper(new IdRowMapper())
        .build();
  }

  static final class IdRowMapper implements RowMapper<Long> {

    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getLong(1);
    }
  }
}
