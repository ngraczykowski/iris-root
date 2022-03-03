package com.silenteight.customerbridge.common.batch;

import com.silenteight.customerbridge.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;

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
