/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AlertMessageProperties.class)
class AlertMessageConfigurationProvider implements AlertMessageConfiguration {

  private final AlertMessageProperties alertMessageProperties;

  @Override
  public long getStoredQueueLimit() {
    return alertMessageProperties.getStoredQueueLimit();
  }

  @Override
  public Duration getDecisionRequestedTime() {
    return alertMessageProperties.getDecisionRequestedTime();
  }

  @Override
  public boolean isOriginalMessageDeletedAfterRecommendation() {
    return alertMessageProperties.isOriginalMessageDeletedAfterRecommendation();
  }

  @Override
  public int getMaxHitsPerAlert() {
    return alertMessageProperties.getMaxHitsPerAlert();
  }
}
