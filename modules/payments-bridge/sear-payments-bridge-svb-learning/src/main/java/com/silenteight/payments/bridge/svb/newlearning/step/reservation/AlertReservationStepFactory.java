package com.silenteight.payments.bridge.svb.newlearning.step.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.step.composite.AlertCompositeReaderFactory;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobParameterExecutionContextCopyListener;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class AlertReservationStepFactory {

  @Language("PostgreSQL")
  private static final String LEARNING_ALERT_QUERY =
      "SELECT learning_alert_id FROM pb_learning_alert";

  private final AlertCompositeReaderFactory alertCompositeReaderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final AlertReservationProperties properties;

  Step createStep(String stepName, String writerInsert, int chunkSize) {
    return stepBuilderFactory
        .get(stepName)
        .listener(new JobParameterExecutionContextCopyListener())
        .chunk(chunkSize)
        .reader(historicalAssessmentCompositeAlertReader())
        .writer(item -> log.debug("writing item: {} using writer: {}", item, writerInsert))
        .build();
  }

  @Bean
  @StepScope
  private AbstractItemStreamItemReader<AlertComposite> historicalAssessmentCompositeAlertReader() {
    return alertCompositeReaderFactory.createAlertCompositeReader(
        LEARNING_ALERT_QUERY, properties.getChunkSize());
  }


}
