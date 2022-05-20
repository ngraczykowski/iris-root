package com.silenteight.payments.bridge.svb.learning.step.remove.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobProperties;
import com.silenteight.payments.bridge.svb.learning.step.remove.IdRowMapper;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.silenteight.payments.bridge.svb.learning.job.remove.RemoveFileDataJobConstants.REMOVE_ACTION_STEP_NAME;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(RemoveFileDataJobProperties.class)
class RemoveActionStepConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT learning_action_id FROM pb_learning_action WHERE fkco_messages "
          + "NOT IN (SELECT fkco_id FROM pb_learning_alert)";

  private final RemoveFileDataJobProperties properties;

  @Bean
  Step removeActionsStep(
      JdbcCursorItemReader<Long> removeLearningActionReader,
      ItemWriter<Long> removeActionWriter,
      StepBuilderFactory stepBuilderFactory) {
    return stepBuilderFactory
        .get(REMOVE_ACTION_STEP_NAME)
        .<Long, Long>chunk(properties.getChunkSize())
        .reader(removeLearningActionReader)
        .writer(removeActionWriter)
        .build();
  }

  @Bean
  @StepScope
  public JdbcCursorItemReader<Long> removeLearningActionReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<Long>()
        .name("actionReader")
        .dataSource(dataSource)
        .sql(QUERY)
        .rowMapper(new IdRowMapper())
        .build();
  }
}
