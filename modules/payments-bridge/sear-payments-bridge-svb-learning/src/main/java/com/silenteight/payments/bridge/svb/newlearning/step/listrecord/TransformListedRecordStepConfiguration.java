package com.silenteight.payments.bridge.svb.newlearning.step.listrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.job.csvstore.StoreCsvJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.step.JpaWriterFactory;
import com.silenteight.payments.bridge.svb.newlearning.step.TransformReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STEP_TRANSFORM_RECORD;

@Configuration
@RequiredArgsConstructor
@Slf4j
class TransformListedRecordStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final JpaWriterFactory writerFactory;
  private final StoreCsvJobProperties properties;

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT"
          + " fkco_id,\n"
          + "       fkco_v_list_fmm_id,\n"
          + "       fkco_v_list_official_ref,\n"
          + "       fkco_v_list_type,\n"
          + "       fkco_v_list_origin,\n"
          + "       fkco_v_list_designation,\n"
          + "       fkco_v_list_pep,\n"
          + "       fkco_v_list_fep,\n"
          + "       fkco_v_list_name,\n"
          + "       fkco_v_list_city,\n"
          + "       fkco_v_list_state,\n"
          + "       fkco_v_list_country,\n"
          + "       fkco_v_list_userdata1,\n"
          + "       fkco_v_list_userdata2,\n"
          + "       fkco_v_list_keyword,\n"
          + "       fkco_v_list_add_info\n"
          +
          "FROM pb_learning_csv_row"
          + " \n"
          +
          "WHERE job_id"
          + " = ?\n"
          + "GROUP BY 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16;";

  @Bean
  @StepScope
  public JdbcCursorItemReader<LearningListedRecordEntity> recordReader(
      TransformReaderFactory readerFactory,
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {
    return readerFactory.createReader(
        LearningListedRecordEntity.class, jobId, QUERY);
  }

  @Bean
  Step transformListedRecordStep(JdbcCursorItemReader<LearningListedRecordEntity> recordReader) {
    return stepBuilderFactory
        .get(STEP_TRANSFORM_RECORD)
        .chunk(properties.getChunkSize())
        .reader(recordReader)
        .writer(writerFactory.createJpaWriter())
        .build();
  }
}
