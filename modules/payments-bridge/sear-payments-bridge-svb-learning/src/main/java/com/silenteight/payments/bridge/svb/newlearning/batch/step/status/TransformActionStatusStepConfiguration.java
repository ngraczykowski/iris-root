package com.silenteight.payments.bridge.svb.newlearning.batch.step.status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.batch.step.JpaWriterFactory;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.LoadCsvJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.TransformReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.STEP_TRANSFORM_STATUS;

@Configuration
@RequiredArgsConstructor
@Slf4j
class TransformActionStatusStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final JpaWriterFactory writerFactory;
  private final LoadCsvJobProperties properties;

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT"
          + " fkco_id,\n"
          + "       fkco_v_status_name,\n"
          + "       fkco_v_status_behavior\n"
          + "FROM pb_learning_csv_row\n"
          + "WHERE job_id = ?\n"
          + "GROUP BY 1, 2, 3;";

  @Bean
  @StepScope
  public JdbcCursorItemReader<LearningActionStatusEntity> statusReader(
      TransformReaderFactory readerFactory,
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {
    return readerFactory.createReader(LearningActionStatusEntity.class, jobId, QUERY);
  }

  @Bean
  Step transformStatusStep(JdbcCursorItemReader<LearningActionStatusEntity> statusReader) {
    return stepBuilderFactory
        .get(STEP_TRANSFORM_STATUS)
        .chunk(properties.getChunkSize())
        .reader(statusReader)
        .writer(writerFactory.createJpaWriter())
        .build();
  }
}
