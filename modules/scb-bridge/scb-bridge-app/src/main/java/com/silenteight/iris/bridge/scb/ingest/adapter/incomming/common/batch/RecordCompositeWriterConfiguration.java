/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.EcmBridgeLearningJobProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RecordCompositeWriterConfiguration {

  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private final EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties;

  @Bean
  @JobScope
  RecordCompositeWriter scbLearningAlertLevelRecordCompositeWriter(
      GnsSyncDeltaService deltaService,
      BatchAlertIngestService ingestService) {

    return LearningRecordCompositeWriter.builder()
        .useDelta(alertLevelLearningJobProperties.isUseDelta())
        .deltaService(deltaService)
        .ingestService(ingestService)
        .deltaJobName(alertLevelLearningJobProperties.getDeltaJobName())
        .build();
  }

  @Bean
  @JobScope
  RecordCompositeWriter ecmLearningRecordCompositeWriter(
      GnsSyncDeltaService deltaService,
      BatchAlertIngestService ingestService) {

    return LearningRecordCompositeWriter.builder()
        .useDelta(ecmBridgeLearningJobProperties.isUseDelta())
        .deltaService(deltaService)
        .ingestService(ingestService)
        .deltaJobName(ecmBridgeLearningJobProperties.getDeltaJobName())
        .build();
  }
}
