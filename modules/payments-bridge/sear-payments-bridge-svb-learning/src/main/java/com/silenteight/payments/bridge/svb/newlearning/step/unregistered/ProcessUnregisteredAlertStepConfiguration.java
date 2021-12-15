package com.silenteight.payments.bridge.svb.newlearning.step.unregistered;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.step.alert.LearningAlertEntity;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

import static com.silenteight.payments.bridge.svb.newlearning.step.unregistered.UnregisteredJobConstants.UNREGISTERED_STEP_NAME;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(UnregisteredJobProperties.class)
class ProcessUnregisteredAlertStepConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY = "SELECT * FROM pb_learning_alert;";

  private final StepBuilderFactory stepBuilderFactory;
  private final UnregisteredJobProperties properties;

  @Bean
  @StepScope
  public JdbcCursorItemReader<LearningAlertEntity> compositeAlertReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<LearningAlertEntity>()
        .name("flatRowsReader")
        .dataSource(dataSource)
        .sql(QUERY)
        .rowMapper(new BeanPropertyRowMapper<>(LearningAlertEntity.class))
        .build();
  }

  @Bean
  Step processUnregisterAlertStep(JdbcCursorItemReader<LearningAlertEntity> compositeAlertReader) {
    return stepBuilderFactory
        .get(UNREGISTERED_STEP_NAME)
        .listener(new JobParameterExecutionContextCopyListener())
        .chunk(properties.getChunkSize())
        .reader(compositeAlertReader)
        .writer(item -> log.debug("writing item: {}", item))
        .build();
  }
}
