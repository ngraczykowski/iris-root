/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.payments.bridge.common.indexing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "pb.feature.bridge-discriminator.enabled", havingValue = "true")
class BridgeDiscriminatorStrategy implements DiscriminatorStrategy {

  @Override
  public String create(String alertMessageId, String systemId, String messageId) {
    return alertMessageId;
  }
}
