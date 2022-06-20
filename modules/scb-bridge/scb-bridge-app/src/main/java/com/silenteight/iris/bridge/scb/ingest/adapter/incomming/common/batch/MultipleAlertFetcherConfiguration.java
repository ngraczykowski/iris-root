/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;

import org.springframework.context.ApplicationEventPublisher;

class MultipleAlertFetcherConfiguration {

  final ScbBridgeConfigProperties configProperties;
  final ApplicationEventPublisher eventPublisher;
  AlertFetcherConfigurationHelper configurationHelper;

  MultipleAlertFetcherConfiguration(
      ScbBridgeConfigProperties configProperties,
      ApplicationEventPublisher eventPublisher,
      GnsSolutionMapper gnsSolutionMapper) {
    this.configProperties = configProperties;
    this.eventPublisher = eventPublisher;
    this.configurationHelper = getConfigurationHelper(gnsSolutionMapper);
  }

  private AlertFetcherConfigurationHelper getConfigurationHelper(
      GnsSolutionMapper gnsSolutionMapper) {
    return new AlertFetcherConfigurationHelper(configProperties, eventPublisher, gnsSolutionMapper);
  }
}
