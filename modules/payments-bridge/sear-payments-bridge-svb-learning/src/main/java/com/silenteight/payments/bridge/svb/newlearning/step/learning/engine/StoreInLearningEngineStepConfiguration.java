package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.job.historical.HistoricalRiskAssessmentJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.step.composite.AlertCompositeReaderFactory;
import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.HISTORICAL_ASSESSMENT_STORE_STEP;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HistoricalRiskAssessmentJobProperties.class)
@Slf4j
class StoreInLearningEngineStepConfiguration {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT learning_alert_id FROM pb_learning_historical_reservation WHERE job_id=?";

  private final StepBuilderFactory stepBuilderFactory;
  private final AlertCompositeReaderFactory alertCompositeReaderFactory;
  private final HistoricalRiskAssessmentJobProperties properties;
  private final StoreInLearningEngineProcessor storeInLearningEngineProcessor;

  @Bean
  @StepScope
  public AbstractItemStreamItemReader<AlertComposite> historicalReservationCompositeReader(
      @Value("#{stepExecution.jobExecution.jobId}") Long jobId) {
    return alertCompositeReaderFactory.createAlertCompositeReader(
        QUERY, jobId, properties.getChunkSize());
  }

  @Bean
  Step storeHistoricalAssessmentInLearningEngineStep(
      AbstractItemStreamItemReader<AlertComposite> historicalReservationCompositeReader) {
    return stepBuilderFactory
        .get(HISTORICAL_ASSESSMENT_STORE_STEP)
        .listener(new JobParameterExecutionContextCopyListener())
        .<AlertComposite, HistoricalDecisionLearningStoreExchangeRequest>chunk(
            properties.getChunkSize())
        .reader(historicalReservationCompositeReader)
        .processor(storeInLearningEngineProcessor)
        .writer(item -> log.info("writing item: {}", item.size()))
        .build();
  }
}
