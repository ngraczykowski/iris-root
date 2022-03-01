package com.silenteight.payments.bridge.svb.learning.step.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.app.LearningProperties;
import com.silenteight.payments.bridge.svb.learning.step.JpaWriterFactory;
import com.silenteight.payments.bridge.svb.learning.step.TransformReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.STEP_TRANSFORM_HIT;

@Configuration
@RequiredArgsConstructor
@Slf4j
class TransformHitStepConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  private final JpaWriterFactory writerFactory;
  private final LearningProperties properties;

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT"
          + " fkco_messages,\n"
          + "       fkco_b_highlight_hit,\n"
          + "       fkco_v_name_matched_text,\n"
          + "       fkco_v_address_matched_text,\n"
          + "       fkco_v_city_matched_text,\n"
          + "       fkco_v_state_matched_text,\n"
          + "       fkco_v_country_matched_text,\n"
          + "       fkco_v_list_matched_name,\n"
          + "       fkco_v_fml_type,\n"
          + "       fkco_i_fml_priority,\n"
          + "       fkco_i_fml_confidentiality,\n"
          + "       fkco_v_hit_match_level,\n"
          + "       fkco_v_hit_type,\n"
          + "       fkco_i_nonblocking,\n"
          + "       fkco_i_blocking,\n"
          + "       fkco_listed_record,\n"
          + "       fkco_filtered_date,\n"
          + "       fkco_d_filtered_datetime_1,\n"
          + "       fkco_v_matched_tag,\n"
          + "       fkco_v_matched_tag_content,\n"
          + "       fkco_i_sequence,\n"
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
          "FROM pb_learning_csv_row\n"
          +
          "WHERE job_id"
          + " = ?\n"
          + "GROUP BY 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, "
          + "16, 17, 18, 19, 20, 21, 22, 23, 24, 25,\n"
          + "         26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36";

  @Bean
  @StepScope
  public JdbcCursorItemReader<LearningHitEntity> hitReader(
      TransformReaderFactory readerFactory,
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {
    return readerFactory.createReader(jobId, QUERY, new LearningHitRowMapper());
  }

  @Bean
  Step transformHitStep(JdbcCursorItemReader<LearningHitEntity> hitReader) {
    return stepBuilderFactory
        .get(STEP_TRANSFORM_HIT)
        .chunk(properties.getChunkSize())
        .reader(hitReader)
        .writer(writerFactory.createJpaWriter())
        .build();
  }
}
