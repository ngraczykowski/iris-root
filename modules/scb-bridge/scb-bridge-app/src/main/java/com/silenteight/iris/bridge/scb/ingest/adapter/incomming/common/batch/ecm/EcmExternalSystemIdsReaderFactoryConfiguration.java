/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.EcmBridgeLearningJobProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
class EcmExternalSystemIdsReaderFactoryConfiguration {

  private final ScbBridgeConfigProperties properties;
  private final EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties;

  @Bean
  @JobScope
  ExternalIdsReaderFactory learningEcmExternalIdsReaderFactory(DataSource ecmDataSource) {

    return new ExternalIdsReaderFactory(
        ecmDataSource,
        ecmBridgeLearningJobProperties.getEcmViewName(),
        properties.getChunkSize(),
        properties.getQueryTimeout()
    );
  }
}
