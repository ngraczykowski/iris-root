/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.AlertComposite;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.MeteringAlertCompositeProcessor;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.RecordCompositeWriter;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class EcmSyncStepsConfiguration {

  private static final String STEP_NAME_PREFIX =
      EcmSyncStepsConfiguration.class.getPackageName() + "#";

  private final StepBuilderFactory stepBuilderFactory;
  private final ScbBridgeConfigProperties properties;
  private final MeteringAlertCompositeProcessor meteringAlertCompositeProcessor;

  @Bean
  public Step collectEcmRecordsLearning(
      EcmRecordCompositeReader learningRecordCompositeReader,
      RecordCompositeWriter ecmLearningRecordCompositeWriter) {
    return createCollectRecordsStep(
        learningRecordCompositeReader,
        ecmLearningRecordCompositeWriter);
  }

  private Step createCollectRecordsStep(
      EcmRecordCompositeReader reader, RecordCompositeWriter writer) {
    return createStep("collectRecordsStep")
        .<AlertComposite, AlertComposite>chunk(properties.getChunkSize())
        .reader(reader)
        .processor(meteringAlertCompositeProcessor)
        .writer(writer)
        .build();
  }

  @NotNull
  private StepBuilder createStep(String stepName) {
    return stepBuilderFactory.get(STEP_NAME_PREFIX + stepName);
  }
}
