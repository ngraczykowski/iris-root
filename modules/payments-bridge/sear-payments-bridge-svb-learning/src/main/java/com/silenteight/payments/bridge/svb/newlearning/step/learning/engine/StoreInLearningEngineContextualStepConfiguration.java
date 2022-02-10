package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.job.etl.EtlJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.step.composite.AlertCompositeReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;

import java.io.IOException;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.CONTEXTUAL_LEARNING_STORE_STEP;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(EtlJobProperties.class)
class StoreInLearningEngineContextualStepConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT learning_alert_id FROM pb_learning_contextual_reservation WHERE job_id=?";

  private final StepBuilderFactory stepBuilderFactory;
  private final AlertCompositeReaderFactory alertCompositeReaderFactory;
  private final EtlJobProperties properties;
  private final StoreInLearningEngineProcessor storeInLearningEngineContextualProcessor;
  private final StoreInLearningEngineWriter storeInLearningEngineWriter;


  @Bean
  @StepScope
  public AbstractItemStreamItemReader<AlertComposite> contextualReservationCompositeReader(
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {
    return alertCompositeReaderFactory.createAlertCompositeReader(
        QUERY, jobId, properties.getChunkSize());
  }

  @Bean
  Step storeContextualLearningInLearningEngineStep(
      AbstractItemStreamItemReader<AlertComposite> contextualReservationCompositeReader) {
    return stepBuilderFactory
        .get(CONTEXTUAL_LEARNING_STORE_STEP)
        .listener(new JobParameterExecutionContextCopyListener())
        .<AlertComposite, HistoricalDecisionLearningAggregate>chunk(properties.getChunkSize())
        .reader(contextualReservationCompositeReader)
        .processor(storeInLearningEngineContextualProcessor)
        .writer(storeInLearningEngineWriter)
        .faultTolerant()
        .retryPolicy(new AlwaysRetryPolicy())
        .retry(IOException.class)
        .retryLimit(properties.getRetryLimit())
        .backOffPolicy(backoffPolicy())
        .skipPolicy(new AlwaysSkipItemSkipPolicy())
        .build();
  }

  private BackOffPolicy backoffPolicy() {
    FixedBackOffPolicy policy = new FixedBackOffPolicy();
    policy.setBackOffPeriod(properties.getRetryPeriodMilliseconds());
    return policy;
  }
}
